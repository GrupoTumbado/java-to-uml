package org.java2uml.java2umlapi.umlComponenets;

import com.github.javaparser.resolution.declarations.ResolvedDeclaration;

import java.util.List;
import java.util.Optional;

import static org.java2uml.java2umlapi.util.StaticParsedComponentsUtil.getVisibilityModifierSymbol;

public class ParsedConstructorComponent implements ParsedComponent {
    private final ResolvedDeclaration resolvedDeclaration;
    private final ParsedComponent parent;
    private final String printableName;
    private final String name;

    public ParsedConstructorComponent(ParsedComponent parent,ResolvedDeclaration resolvedDeclaration) {
        this.resolvedDeclaration = resolvedDeclaration;
        this.parent = parent;
        this.printableName = resolvedDeclaration.asMethod().getSignature();
        this.name = resolvedDeclaration.getName();
    }

    @Override
    public Optional<ResolvedDeclaration> getResolvedDeclaration() {
        return Optional.of(resolvedDeclaration);
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public boolean isParsedConstructorComponent() {
        return true;
    }

    @Override
    public Optional<ParsedConstructorComponent> asParsedConstructorComponent() {
        return Optional.of(this);
    }

    @Override
    public Optional<ParsedComponent> getParent() {
        return Optional.of(parent);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getVisibilityModifierSymbol(resolvedDeclaration) + " [Constructor] " + printableName;
    }


}
