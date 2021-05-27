package org.java2uml.java2umlapi.restControllers.LWControllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.java2uml.java2umlapi.lightWeight.Constructor;
import org.java2uml.java2umlapi.lightWeight.LightWeight;
import org.java2uml.java2umlapi.lightWeight.repository.ConstructorRepository;
import org.java2uml.java2umlapi.lightWeight.repository.LightWeightRepository;
import org.java2uml.java2umlapi.modelAssemblers.ConstructorAssembler;
import org.java2uml.java2umlapi.restControllers.SwaggerDescription;
import org.java2uml.java2umlapi.restControllers.error.ErrorResponse;
import org.java2uml.java2umlapi.restControllers.exceptions.LightWeightNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.java2uml.java2umlapi.restControllers.SwaggerDescription.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * <p>
 * Rest Controller for constructor entities.
 * </p>
 *
 * @author kawaiifox
 */
@RestController
@Tag(name = "Constructor", description = "represents constructor in a source code.")
@RequestMapping("/api/constructor")
public class ConstructorController {

    private final ConstructorRepository constructorRepository;
    private final ConstructorAssembler assembler;
    private final LightWeightRepository lightWeightRepository;

    public ConstructorController(
            ConstructorRepository constructorRepository,
            ConstructorAssembler assembler,
            LightWeightRepository lightWeightRepository
    ) {
        this.constructorRepository = constructorRepository;
        this.assembler = assembler;
        this.lightWeightRepository = lightWeightRepository;
    }

    /**
     * Retrieves constructor entity for provided constructor id
     *
     * @param constructorId id associated with the constructor.
     * @return Entity model of constructor with useful links.
     * @throws LightWeightNotFoundException if constructor is not found.
     */
    @Operation(summary = "Get Constructor", description = "get constructor by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = OK_200_RESPONSE),
            @ApiResponse(responseCode = "404", description = NOT_FOUND_404,
                    content = @Content(mediaType = ERR_RESPONSE_MEDIA_TYPE,
                            schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/{constructorId}")
    public EntityModel<Constructor> one(
            @Parameter(description = SwaggerDescription.CONSTRUCTOR_ID_DESC)
            @PathVariable("constructorId") Long constructorId) {
        return assembler.toModel(
                constructorRepository.findById(constructorId)
                        .orElseThrow(
                                () -> new LightWeightNotFoundException(
                                        "Unable to fetch constructor with id: " + constructorId
                                )
                        )
        );
    }

    /**
     * Retrieves all the constructors associated with the provided parent id.
     *
     * @param parentId id of the parent
     * @return Collection model of constructors with useful links.
     * @throws LightWeightNotFoundException if parent cannot be found.
     */
    @Operation(summary = "Get Constructors", description = "get all constructors by parent id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = OK_200_RESPONSE),
            @ApiResponse(responseCode = "404", description = NOT_FOUND_404,
                    content = @Content(mediaType = ERR_RESPONSE_MEDIA_TYPE,
                            schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/by-parent/{parentId}")
    public CollectionModel<EntityModel<Constructor>> allByParent(@Parameter(name = PARENT_ID_DESC)
                                                                 @PathVariable("parentId") Long parentId) {
        var parent = lightWeightRepository.findById(parentId)
                .orElseThrow(() -> new LightWeightNotFoundException("Unable to fetch parent with id: " + parentId));
        return toCollectionModel(parent);
    }

    /**
     * Converts list of entities to collection model and adds extra links to it.
     *
     * @param parent parent of the entities.
     * @return collection model with added links.
     */
    private CollectionModel<EntityModel<Constructor>> toCollectionModel(LightWeight parent) {
        return assembler.toCollectionModel(constructorRepository.findConstructorByParent(parent))
                .addIf(
                        parent.asClassOrInterface().isPresent(),
                        () -> linkTo(methodOn(ClassOrInterfaceController.class).one(parent.getId())).withRel("parent"))
                .addIf(
                        parent.asEnumLW().isPresent(),
                        () -> linkTo(methodOn(EnumLWController.class).one(parent.getId())).withRel("parent")
                );
    }
}
