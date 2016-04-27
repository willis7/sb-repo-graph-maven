package io.byteshifter.depsgraph.controller

import io.byteshifter.depsgraph.domain.Dependency
import io.byteshifter.depsgraph.domain.DependencyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.web.bind.annotation.RequestMethod.POST

/**
 * @author Sion Williams
 */
@RestController
class DependencyController {
    @Autowired
    DependencyRepository dependencyRepository

    @RequestMapping(value = "/dependency", method = POST)
    ResponseEntity<?> createDependency(@RequestBody Dependency dependency) {
        dependency = dependencyRepository.save(dependency)

        HttpHeaders headers = new HttpHeaders()
        URI newDependencyUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dependency.getId())
                .toUri()
        headers.setLocation(newDependencyUri)
        return new ResponseEntity<>(null, headers, CREATED)
    }
}
