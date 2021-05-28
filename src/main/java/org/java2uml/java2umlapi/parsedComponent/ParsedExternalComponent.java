package org.java2uml.java2umlapi.parsedComponent;

import com.github.javaparser.resolution.declarations.ResolvedDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.java2uml.java2umlapi.visitors.Visitor;

import java.util.Optional;

/**
 * <p>
 * Any classOrInterfaceDeclaration or annotationDeclaration or enumDeclaration
 * which does not belong to the project being parsed and is dependency of the project being parsed is stored in
 * this component.
 * </p>
 *
 * @author kawaiifox
 */
public class ParsedExternalComponent implements ParsedCompositeComponent {

    private final ResolvedTypeDeclaration resolvedTypeDeclaration;

    private final String name;
    private final String packageName;

    /**
     * Initializes ParsedExternalComponent with a resolvedTypeDeclaration.
     * @param resolvedTypeDeclaration it is resolvedTypeDeclaration which is obtained after type solving.
     *                                (Provided by parser in our case.)
     */
    public ParsedExternalComponent(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        this.resolvedTypeDeclaration = resolvedTypeDeclaration;
        this.name = resolvedTypeDeclaration.getQualifiedName();
        this.packageName = resolvedTypeDeclaration.getPackageName();
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public boolean isParsedExternalAncestor() {
        return true;
    }

    @Override
    public Optional<ParsedComponent> getParent() {
        return Optional.empty();
    }

    @Override
    public Optional<ResolvedDeclaration> getResolvedDeclaration() {
        return Optional.of(resolvedTypeDeclaration);
    }

    public ResolvedTypeDeclaration getResolvedTypeDeclaration() {
        return resolvedTypeDeclaration;
    }

    /**
     * @return returns Optional.empty() if this component is not ParsedExternalComponent
     */
    @Override
    public Optional<ParsedExternalComponent> asParsedExternalComponent() {
        return Optional.of(this);
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * @return package name of the type.
     */
    @Override
    public String getPackageName() {
        return packageName;
    }

    /**
     * Accepts a visitor and returns whatever is returned by the visitor.
     *
     * @param v v is the Visitor
     * @return data extracted by visitor.
     */
    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ParsedExternalComponent)) return false;

        ParsedExternalComponent that = (ParsedExternalComponent) o;

        return new EqualsBuilder()
                .append(getName(), that.getName())
                .append(getPackageName(), that.getPackageName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getName())
                .append(getPackageName())
                .toHashCode();
    }

    @Override
    public String toString() {
        return "ParsedExternalComponent{" +
                "name='" + name + '\'' +
                '}';
    }

}
