package br.edu.ifsp.contatopdm.controller

import androidx.room.Room
import br.edu.ifsp.contatopdm.model.Contact
import br.edu.ifsp.contatopdm.model.ContactDao
import br.edu.ifsp.contatopdm.model.ContactDaoRoom
import br.edu.ifsp.contatopdm.model.ContactDaoSqlite
import br.edu.ifsp.contatopdm.view.MainActivity

class ContactController(private val mainActivity: MainActivity) {
    //private val contactDaoImpl: ContactDao = ContactDaoSqlite(mainActivity)
    private val contactDaoImpl: ContactDao = Room.databaseBuilder(
        mainActivity, ContactDaoRoom::class.java, ContactDaoRoom.CONTACT_DATABASE_FILE
    ).build().getContactDao()

    fun insertContact(contact: Contact) = contactDaoImpl.createContact(contact)
    fun getContact(id: Int) = contactDaoImpl.retrieveContact(id)
    fun getContacts() {
        Thread {
            mainActivity.updateContactList(contactDaoImpl.retrieveContacts())
        }.start()
    }
    fun editContact(contact: Contact) = contactDaoImpl.updateContact(contact)
    fun removeContact(contact: Contact) = contactDaoImpl.deleteContact(contact)
}