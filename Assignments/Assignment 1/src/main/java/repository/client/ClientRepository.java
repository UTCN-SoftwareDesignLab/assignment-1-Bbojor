package repository.client;

import model.Client;
import model.validation.Notification;

import java.util.List;

public interface ClientRepository {

    public List<Client> getAll();

    public boolean addClient(Client client);

    public Notification<Client> getClientById(Long id);

    public boolean updateClient(Client client);

    public boolean deleteClient(Client client);

    public void deleteAll();
}
