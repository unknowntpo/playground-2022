package org.example;

import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("org.example.AutoBuilder")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class BuilderProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "BuilderProcessor: processing started");
        
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(AutoBuilder.class)) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, 
                "BuilderProcessor: found annotated element: " + annotatedElement.getSimpleName());
            
            if (annotatedElement.getKind() != ElementKind.CLASS) {
                processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR, 
                    "@AutoBuilder can only be applied to classes", 
                    annotatedElement
                );
                return true;
            }

            try {
                generateBuilderClass((TypeElement) annotatedElement);
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, 
                    "BuilderProcessor: successfully generated builder for " + annotatedElement.getSimpleName());
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR, 
                    "Failed to generate builder class: " + e.getMessage(), 
                    annotatedElement
                );
            }
        }
        return true;
    }

    private void generateBuilderClass(TypeElement typeElement) throws IOException {
        String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
        String className = typeElement.getSimpleName().toString();
        String builderClassName = className + "Builder";

        List<VariableElement> fields = typeElement.getEnclosedElements()
                .stream()
                .filter(element -> element.getKind() == ElementKind.FIELD)
                .filter(element -> !element.getModifiers().contains(Modifier.STATIC))
                .map(element -> (VariableElement) element)
                .collect(Collectors.toList());

        TypeSpec.Builder builderClass = TypeSpec.classBuilder(builderClassName)
                .addModifiers(Modifier.PUBLIC);

        // Add fields to builder
        for (VariableElement field : fields) {
            builderClass.addField(
                FieldSpec.builder(TypeName.get(field.asType()), field.getSimpleName().toString())
                    .addModifiers(Modifier.PRIVATE)
                    .build()
            );
        }

        // Add setter methods
        for (VariableElement field : fields) {
            String fieldName = field.getSimpleName().toString();
            String setterName = fieldName;
            
            MethodSpec setter = MethodSpec.methodBuilder(setterName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(ClassName.get(packageName, builderClassName))
                    .addParameter(TypeName.get(field.asType()), fieldName)
                    .addStatement("this.$N = $N", fieldName, fieldName)
                    .addStatement("return this")
                    .build();
            
            builderClass.addMethod(setter);
        }

        // Add build method - assume constructor takes fields in the same order they are declared
        MethodSpec.Builder buildMethod = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(typeElement));

        if (!fields.isEmpty()) {
            StringBuilder statement = new StringBuilder("return new $T(");
            for (int i = 0; i < fields.size(); i++) {
                if (i > 0) statement.append(", ");
                statement.append("this.$N");
            }
            statement.append(")");
            
            Object[] args = new Object[fields.size() + 1];
            args[0] = ClassName.get(typeElement);
            for (int i = 0; i < fields.size(); i++) {
                args[i + 1] = fields.get(i).getSimpleName().toString();
            }
            
            buildMethod.addStatement(statement.toString(), args);
        } else {
            buildMethod.addStatement("return new $T()", ClassName.get(typeElement));
        }

        builderClass.addMethod(buildMethod.build());

        TypeSpec builderTypeSpec = builderClass.build();

        JavaFile javaFile = JavaFile.builder(packageName, builderTypeSpec)
                .build();

        javaFile.writeTo(processingEnv.getFiler());
    }
}