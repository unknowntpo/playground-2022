import * as ts from "typescript";
import * as fs from "fs";

function parseTypeScriptFile(filePath: string): ts.SourceFile {
  const content = fs.readFileSync(filePath, "utf-8");
  return ts.createSourceFile(filePath, content, ts.ScriptTarget.Latest, true);
}

function extractInterfaces(
  sourceFile: ts.SourceFile,
): Map<string, ts.InterfaceDeclaration> {
  const interfaces = new Map<string, ts.InterfaceDeclaration>();

  function visit(node: ts.Node) {
    if (ts.isInterfaceDeclaration(node)) {
      interfaces.set(node.name.text, node);
    }
    ts.forEachChild(node, visit);
  }

  visit(sourceFile);
  return interfaces;
}

function compareInterfaces(
  interfaceA: ts.InterfaceDeclaration,
  interfaceB: ts.InterfaceDeclaration,
): string[] {
  const differences: string[] = [];

  const propertiesA = new Map(
    interfaceA.members
      .filter(ts.isPropertySignature)
      .map((prop) => [prop.name.getText(), prop]),
  );

  const propertiesB = new Map(
    interfaceB.members
      .filter(ts.isPropertySignature)
      .map((prop) => [prop.name.getText(), prop]),
  );

  for (const [name, propA] of propertiesA) {
    if (!propertiesB.has(name)) {
      differences.push(`Property '${name}' is missing in the second interface`);
    } else {
      const propB = propertiesB.get(name)!;
      if (propA.type?.getText() !== propB.type?.getText()) {
        differences.push(
          `Property '${name}' has different types: ${propA.type?.getText()} vs ${propB.type?.getText()}`,
        );
      }
    }
  }

  for (const name of propertiesB.keys()) {
    if (!propertiesA.has(name)) {
      differences.push(`Property '${name}' is missing in the first interface`);
    }
  }

  return differences;
}

function compareFiles(file1: string, file2: string) {
  const sourceFile1 = parseTypeScriptFile(file1);
  const sourceFile2 = parseTypeScriptFile(file2);

  const interfaces1 = extractInterfaces(sourceFile1);
  const interfaces2 = extractInterfaces(sourceFile2);

  const allInterfaceNames = new Set([
    ...interfaces1.keys(),
    ...interfaces2.keys(),
  ]);

  for (const name of allInterfaceNames) {
    if (interfaces1.has(name) && interfaces2.has(name)) {
      const differences = compareInterfaces(
        interfaces1.get(name)!,
        interfaces2.get(name)!,
      );
      if (differences.length > 0) {
        console.log(`[FAIL] Differences in interface '${name}':`);
        differences.forEach((diff) => console.log(`  - ${diff}`));
      } else {
        console.log(`[PASS] Interface '${name}' is identical in both files`);
      }
    } else if (interfaces1.has(name)) {
      console.log(
        `[FAIL] Interface '${name}' is present only in the first file`,
      );
    } else {
      console.log(
        `[FAIL] Interface '${name}' is present only in the second file`,
      );
    }
  }
}

export { compareFiles };
