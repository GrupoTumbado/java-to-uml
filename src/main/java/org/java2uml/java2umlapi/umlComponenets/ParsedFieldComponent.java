package org.java2uml.java2umlapi.umlComponenets;

import com.github.javaparser.resolution.declarations.ResolvedDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import org.java2uml.java2umlapi.util.umlSymbols.UMLModifier;
import org.java2uml.java2umlapi.util.umlSymbols.VisibilityModifierSymbol;

import java.util.Optional;

/**
 * <p>
 * Leaf component representing FieldDeclarations in java source code.
 * </p>
 *
 * @author kawaiifox
 */
public class ParsedFieldComponent implements ParsedComponent {
    private final ParsedComponent parent;
    private final ResolvedFieldDeclaration resolvedDeclaration;
    private final String name;

    /**
     * Initializes ParsedFieldComponent.
     * @param parent Parent of this component.
     * @param resolvedDeclaration resolvedFieldDeclaration is type solved field declaration
     *                            retrieved from resolvedReferenceTypeDeclaration.
     */
    public ParsedFieldComponent(ParsedComponent parent, ResolvedFieldDeclaration resolvedDeclaration) {
        this.parent = parent;
        this.resolvedDeclaration = resolvedDeclaration;
        this.name = resolvedDeclaration.asField().getName();
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public boolean isParsedFieldComponent() {
        return true;
    }

    @Override
    public Optional<ParsedFieldComponent> asParsedFieldComponent() {
        return Optional.of(this);
    }

    @Override
    public Optional<ResolvedDeclaration> getResolvedDeclaration() {
        return Optional.of(resolvedDeclaration);
    }

    @Override
    public Optional<ParsedComponent> getParent() {
        return Optional.of(parent);
    }

    @Override
    public String getName() {
        return name;
    }

    private String getClassOfField() {
        if (resolvedDeclaration.getType().isReferenceType()) {
            var list = resolvedDeclaration.getType().asReferenceType().getQualifiedName().split("\\.");
            return list[list.length - 1];
        }

        return resolvedDeclaration.getType().asPrimitive().name()
                + (resolvedDeclaration.getType().asPrimitive().isArray() ? "[]" : "");
    }

    @Override
    public String toUML() {
        return VisibilityModifierSymbol.of(resolvedDeclaration.accessSpecifier().asString()) + " " + getClassOfField() + " "
                + (resolvedDeclaration.isStatic() ? UMLModifier.STATIC : "")
                + " " + name;
    }

    @Override
    public String toString() {
        return "ParsedFieldComponent{" +
                "name='" + name + '\'' +
                '}';
    }
}
