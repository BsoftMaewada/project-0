package com.revature.service;

import com.revature.dao.ClientDao;
import com.revature.exception.ClientNotFoundException;
import com.revature.model.Client;

import java.sql.SQLException;
import java.util.List;

public class ClientService {

    private ClientDao clientdao;

    public ClientService() {
        this.clientdao = new ClientDao();
    }

    public ClientService(ClientDao mockDao) {
        this.clientdao = mockDao;
    }

    public List<Client> getAllClients() throws SQLException {
        return this.clientdao.getAllClients();
    }

    public Client updateClient(String id, Client c) {
        try {
            int studentId = Integer.parseInt(id);

            if (clientdao.getClientById(studentId) == null) {
                throw new ClientNotFoundException("User is trying to edit a Student that does not exist. Student with id " + studentId
                        + " was not found");
            }

            validateClientInformation(c);

            c.setId(studentId);
            Client editedStudent = clientdao.updateClient(c);

            return editedStudent;
        } catch(NumberFormatException | ClientNotFoundException | SQLException e) {
            throw new IllegalArgumentException("Id provided for client must be a valid int");
        }
    }


    public Client createClient(Client c) throws SQLException{
        validateClientInformation(c);

        Client clientToAdd =  clientdao.createClient(c);
        return clientToAdd;
    }


    public boolean deleteClientById(String id) throws SQLException, ClientNotFoundException {

        try {

            int clientId = Integer.parseInt(id);

            boolean c = clientdao.deleteClientById(clientId);

            return c;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Id provided for client must be a valid int");
        }
    }


    public Client getClientById(String id) throws SQLException, ClientNotFoundException {
        try {
            int clientId = Integer.parseInt(id);

            Client c = clientdao.getClientById(clientId);

            if (c == null) {
                throw new ClientNotFoundException("Client with id " + clientId + " was not found");
            }

            return c;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Id provided for client must be a valid int");
        }
    }

    private void validateClientInformation(Client c) {
        c.setFirstName(c.getFirstName().trim());
        c.setLastName(c.getLastName().trim());

        if (!c.getFirstName().matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("First name must only have alphabetical characters. First name input was " + c.getFirstName());
        }

        if (!c.getLastName().matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("Last name must only have alphabetical characters. Last name input was " + c.getLastName());
        }

        if (c.getAge() < 0) {
            throw new IllegalArgumentException("Adding a student with age < 0 is not valid. Age provided was " + c.getAge());
        }
    }
}