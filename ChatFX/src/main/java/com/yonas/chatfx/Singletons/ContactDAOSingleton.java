package com.yonas.chatfx.Singletons;
import com.yonas.chatfx.DAOs.Interfaces.IContactDAO;
import com.yonas.chatfx.DAOs.Impl.ContactDAO;

public class ContactDAOSingleton {
    private static final IContactDAO contactDAOInstance = new ContactDAO();

    public static IContactDAO getContactDAO() {
        return contactDAOInstance;
    }
}