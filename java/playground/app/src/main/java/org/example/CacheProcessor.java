package org.example;

import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

@SupportedAnnotationTypes("org.example.CacheEnabled")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class CacheProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Types typeUtils;
    private Messager messager;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        elementUtils = env.getElementUtils();
        typeUtils = env.getTypeUtils();
        messager = env.getMessager();
        filer = env.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE, "CacheProcessor running...");
        
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(
            elementUtils.getTypeElement("org.example.CacheEnabled"));
        messager.printMessage(Diagnostic.Kind.NOTE, "Found " + elements.size() + " @CacheEnabled elements");
        
        for (Element element : elements) {
            if (element.getKind() == ElementKind.CLASS) {
                messager.printMessage(Diagnostic.Kind.NOTE, "Processing class: " + element.getSimpleName());
                try {
                    generateCachedClass((TypeElement) element);
                } catch (IOException e) {
                    messager.printMessage(Diagnostic.Kind.ERROR, 
                        "Error generating cached class: " + e.getMessage(), element);
                }
            }
        }
        return true;
    }

    private void generateCachedClass(TypeElement classElement) throws IOException {
        String packageName = elementUtils.getPackageOf(classElement).getQualifiedName().toString();
        String className = classElement.getSimpleName().toString();
        String newClassName = className + "Cached";

        // Find interface that this class implements
        TypeMirror interfaceType = null;
        for (TypeMirror iface : classElement.getInterfaces()) {
            interfaceType = iface;
            break; // Take first interface for simplicity
        }

        if (interfaceType == null) {
            messager.printMessage(Diagnostic.Kind.ERROR, 
                "Class must implement an interface to be cached", classElement);
            return;
        }

        // Build the cached wrapper class
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(newClassName)
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(TypeName.get(interfaceType))
            .addAnnotation(AnnotationSpec.builder(ClassName.get("javax.annotation.processing", "Generated"))
                .addMember("value", "$S", "org.example.CacheProcessor")
                .build());

        // Add delegate field
        FieldSpec delegateField = FieldSpec.builder(TypeName.get(classElement.asType()), "delegate", Modifier.PRIVATE, Modifier.FINAL)
            .build();
        classBuilder.addField(delegateField);

        // Add constructor
        MethodSpec constructor = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(TypeName.get(classElement.asType()), "delegate")
            .addStatement("this.delegate = delegate")
            .build();
        classBuilder.addMethod(constructor);

        // Add cached methods
        for (Element enclosed : classElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.METHOD) {
                ExecutableElement method = (ExecutableElement) enclosed;
                // Check for @Cache annotation by name
                boolean hasCache = method.getAnnotationMirrors().stream()
                    .anyMatch(am -> am.getAnnotationType().toString().equals("org.example.Cache"));
                if (hasCache && method.getModifiers().contains(Modifier.PUBLIC)) {
                    classBuilder.addMethod(generateCachedMethod(method, className));
                }
            }
        }

        // Write the file
        TypeSpec cachedClass = classBuilder.build();
        JavaFile javaFile = JavaFile.builder(packageName, cachedClass).build();
        
        JavaFileObject sourceFile = filer.createSourceFile(packageName + "." + newClassName);
        try (Writer writer = sourceFile.openWriter()) {
            javaFile.writeTo(writer);
        }
        
        messager.printMessage(Diagnostic.Kind.NOTE, 
            "Generated cached wrapper: " + packageName + "." + newClassName);
    }

    private MethodSpec generateCachedMethod(ExecutableElement method, String originalClassName) {
        String methodName = method.getSimpleName().toString();
        TypeName returnType = TypeName.get(method.getReturnType());

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(returnType);

        // Add parameters
        List<String> paramNames = new ArrayList<>();
        for (VariableElement param : method.getParameters()) {
            String paramName = param.getSimpleName().toString();
            methodBuilder.addParameter(TypeName.get(param.asType()), paramName);
            paramNames.add(paramName);
        }

        // Generate caching logic
        String methodKey = originalClassName + "." + methodName;
        String paramKey = paramNames.isEmpty() ? "\"()\"" : 
            "java.util.Arrays.toString(new Object[]{" + String.join(", ", paramNames) + "})";

        methodBuilder.addStatement("String methodKey = $S", methodKey);
        methodBuilder.addStatement("String paramKey = " + paramKey);

        methodBuilder.beginControlFlow("if ($T.contains(methodKey, paramKey))", 
            ClassName.get("org.example", "CacheStorage"));
        
        if (!returnType.equals(TypeName.VOID)) {
            methodBuilder.addStatement("return ($T) $T.get(methodKey, paramKey)", 
                returnType, ClassName.get("org.example", "CacheStorage"));
        } else {
            methodBuilder.addStatement("return");
        }
        methodBuilder.endControlFlow();

        // Call delegate method
        String delegateCall = "delegate." + methodName + "(" + String.join(", ", paramNames) + ")";
        if (!returnType.equals(TypeName.VOID)) {
            methodBuilder.addStatement("$T result = " + delegateCall, returnType);
            methodBuilder.addStatement("$T.put(methodKey, paramKey, result)", 
                ClassName.get("org.example", "CacheStorage"));
            methodBuilder.addStatement("return result");
        } else {
            methodBuilder.addStatement(delegateCall);
            methodBuilder.addStatement("$T.put(methodKey, paramKey, null)", 
                ClassName.get("org.example", "CacheStorage"));
        }

        return methodBuilder.build();
    }
}