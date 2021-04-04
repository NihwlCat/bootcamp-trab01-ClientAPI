package br.pedro.clientAPI.resources;


import br.pedro.clientAPI.dto.ClientDTO;
import br.pedro.clientAPI.entities.Client;
import br.pedro.clientAPI.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping (value = "/clients")
public class ClientResource {

    @Autowired
    private ClientService service;

    @GetMapping
    public ResponseEntity<Page<ClientDTO>> findAll(
            @RequestParam (value = "page", defaultValue = "0") Integer page,
            @RequestParam (value = "linesPerPage", defaultValue = "6") Integer lines,
            @RequestParam (value = "direction", defaultValue = "DESC") String direction,
            @RequestParam (value = "orderBy", defaultValue = "name")  String order){

        PageRequest pageRequest = PageRequest.of(page,lines, Sort.Direction.valueOf(direction), order);
        Page<ClientDTO> dtoClients = service.findAllPaged(pageRequest);

        return ResponseEntity.ok().body(dtoClients);
    }

    @GetMapping (value = "/{id}")
    public ResponseEntity<ClientDTO> findById(@PathVariable Long id){
            return ResponseEntity.ok().body(service.findById(id));
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<String> delete (@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.ok().body("Successfully deleted");
    }

    @PostMapping
    public ResponseEntity<ClientDTO> insert (@RequestBody ClientDTO request){
        request = service.insert(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(request.getId()).toUri();
        return ResponseEntity.created(uri).body(request);
    }

    @PutMapping (value = "/{id}")
    public ResponseEntity<ClientDTO> update (@PathVariable Long id, @RequestBody ClientDTO dto){
        dto = service.update(id,dto);
        return ResponseEntity.ok().body(dto);
    }
}
