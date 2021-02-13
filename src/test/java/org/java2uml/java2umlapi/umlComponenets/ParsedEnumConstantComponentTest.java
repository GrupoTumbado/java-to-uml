package org.java2uml.java2umlapi.umlComponenets;

import com.github.javaparser.resolution.declarations.ResolvedEnumConstantDeclaration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
@DisplayName("When using parsedEnumConstantComponent,")
@ExtendWith(MockitoExtension.class)
class ParsedEnumConstantComponentTest {

    @Mock
    ResolvedEnumConstantDeclaration resolvedEnumConstantDeclaration;

    @Mock
     ParsedEnumComponent parsedEnumComponent;

    ParsedEnumConstantComponent parsedEnumConstantComponent;

    private final String name = "ENUM_TEST";

    @BeforeEach
    void setUp() {
        doReturn(name).when(resolvedEnumConstantDeclaration).getName();
        parsedEnumConstantComponent = new ParsedEnumConstantComponent(resolvedEnumConstantDeclaration, parsedEnumComponent);
    }

    @DisplayName("using getName() should return enum's name.")
    @Test
    void testGetName() {
        assertEquals(name, parsedEnumConstantComponent.getName());
    }

    @Test
    @DisplayName("using toUML() should return enum's uml syntax.")
    void testToUML() {
        assertEquals(name, parsedEnumConstantComponent.getName());
    }

}