package org.java2uml.java2umlapi.parsedComponent;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import org.java2uml.java2umlapi.util.umlSymbols.VisibilityModifierSymbol;
import org.java2uml.java2umlapi.visitors.umlExtractor.UMLExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for ParsedFieldComponent")
class ParsedFieldComponentTest {
    @Mock
    ResolvedFieldDeclaration resolvedFieldDeclaration;
    ParsedFieldComponent parsedFieldComponent;

    private final String fieldName = "testField";
    private final String parentName = "co.test";

    @BeforeEach
    void setUp() {
        assertNotNull(resolvedFieldDeclaration, "ResolvedFieldDeclaration is null.");

        doReturn(fieldName).when(resolvedFieldDeclaration).getName();

        var parent = mock(ParsedComponent.class);
        doReturn(parentName).when(parent).getName();
        parsedFieldComponent = new ParsedFieldComponent(parent, resolvedFieldDeclaration);
    }

    @Test
    @DisplayName("When using getName(), parsedFieldComponent should return name.")
    void testGetName() {
        assertEquals(parentName + "." + fieldName, parsedFieldComponent.getName());
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @DisplayName("When Field's type is a reference type,")
    class FieldIsReferenceTypeTest {
        @Mock
        ResolvedFieldDeclaration resolvedFieldDeclaration;


        ParsedFieldComponent parsedFieldComponent;

        private final String fieldName = "testField";
        private final String accessSpecifierStr = "private";
        private final String qualifiedName = "java.lang.String";
        private final boolean isStatic = true;

        @BeforeEach
        void setUp() {
            doReturn(fieldName).when(resolvedFieldDeclaration).getName();

            var resolvedReferenceType = mock(ResolvedReferenceType.class);
            doReturn(qualifiedName).when(resolvedReferenceType).getQualifiedName();

            var resolvedType = mock(ResolvedType.class);
            doReturn(true).when(resolvedType).isReferenceType();
            doReturn(resolvedReferenceType).when(resolvedType).asReferenceType();

            var accessSpecifier = mock(AccessSpecifier.class);
            doReturn(accessSpecifierStr).when(accessSpecifier).asString();

            doReturn(resolvedType).when(resolvedFieldDeclaration).getType();
            doReturn(accessSpecifier).when(resolvedFieldDeclaration).accessSpecifier();
            doReturn(isStatic).when(resolvedFieldDeclaration).isStatic();

            var parent = mock(ParsedComponent.class);
            doReturn(parentName).when(parent).getName();

            parsedFieldComponent = new ParsedFieldComponent(parent, resolvedFieldDeclaration);
        }

        @Test
        @DisplayName("using umlExtractor on this component, should return uml syntax for field.")
        void testToUML() {
            var uml = parsedFieldComponent.accept(new UMLExtractor());
            var typeStr = qualifiedName.split("\\.");

            assertTrue(uml.contains(typeStr[typeStr.length - 1]), "generated uml syntax does not contain correct type");
            assertTrue(uml.contains(fieldName), "generated uml syntax does not contain correct field name");
            assertEquals(uml.contains("static"), isStatic);
            assertTrue(uml.contains(VisibilityModifierSymbol.of(accessSpecifierStr).toString()));
        }
    }

    @Nested
    @DisplayName("When Field's type is a primitive type,")
    @ExtendWith(MockitoExtension.class)
    class FieldIsPrimitiveTypeTest {
        @Mock
        ResolvedFieldDeclaration resolvedFieldDeclaration;


        ParsedFieldComponent parsedFieldComponent;

        private final String fieldName = "testField";
        private final String accessSpecifierStr = "private";
        private final String primitiveTypeName = "int";
        private final boolean isStatic = true;

        @BeforeEach
        void setUp() {
            doReturn(fieldName).when(resolvedFieldDeclaration).getName();

            var resolvedType = mock(ResolvedType.class);
            doReturn(primitiveTypeName).when(resolvedType).describe();
            doReturn(false).when(resolvedType).isReferenceType();

            var accessSpecifier = mock(AccessSpecifier.class);
            doReturn(accessSpecifierStr).when(accessSpecifier).asString();

            doReturn(resolvedType).when(resolvedFieldDeclaration).getType();
            doReturn(accessSpecifier).when(resolvedFieldDeclaration).accessSpecifier();
            doReturn(isStatic).when(resolvedFieldDeclaration).isStatic();

            var parent = mock(ParsedComponent.class);
            doReturn(parentName).when(parent).getName();

            parsedFieldComponent = new ParsedFieldComponent(parent, resolvedFieldDeclaration);
        }

        @Test
        @DisplayName("using umlExtractor on this component, should return uml syntax for field.")
        void testToUML() {
            var uml = parsedFieldComponent.accept(new UMLExtractor());

            assertTrue(uml.contains(primitiveTypeName));
            assertTrue(uml.contains(fieldName));
            assertEquals(uml.contains("static"), isStatic);
            assertTrue(uml.contains(VisibilityModifierSymbol.of(accessSpecifierStr).toString()));
        }
    }
}