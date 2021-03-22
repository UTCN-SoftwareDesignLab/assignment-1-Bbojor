package service.client;

import model.Client;
import model.validation.Notification;

import java.util.List;

public interface ClientService {

     public List<Client> getAll();

     public Notification<Boolean> addClient(Client client);

     public Notification<Boolean> updateClient(Client client);

     public Notification<Boolean> deleteClient(Client client);

     public void deleteAll();

}
