package br.pedro.clientAPI.services;

import br.pedro.clientAPI.dto.ClientDTO;
import br.pedro.clientAPI.entities.Client;
import br.pedro.clientAPI.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;


@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Transactional (readOnly = true)
    public Page<ClientDTO> findAllPaged(PageRequest pageRequest){
        Page<Client> clients = repository.findAll(pageRequest);
        return clients.map(ClientDTO::new);
    }

    @Transactional (readOnly = true)
    public ClientDTO findById(Long id){
        Optional<Client> obj = repository.findById(id);
        Client client = obj.orElseThrow(() -> new EntityNotFoundException("Entity was not found"));

        return new ClientDTO(client);
    }

    @Transactional
    public void deleteById(Long id) {
        try{
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new EntityNotFoundException("There is no entity with id: " + id);
        }
    }

    @Transactional
    public ClientDTO insert(ClientDTO dto){
        Client client = new Client(
                null,
                dto.getName(),
                dto.getCpf(),
                dto.getIncome(),
                dto.getBirthDate(),
                dto.getChildren()
        );

        return new ClientDTO(repository.save(client));
    }

    @Transactional
    public ClientDTO update(Long id, ClientDTO dto){
        try {
            Client client = repository.getOne(id);
            client.setName(dto.getName());
            client.setCpf(dto.getCpf());
            client.setBirthDate(dto.getBirthDate());
            client.setIncome(dto.getIncome());

            return new ClientDTO(repository.save(client));
        } catch (EntityNotFoundException e){
            throw new EntityNotFoundException("There is no entity with id: " + id);
        }
    }

}
