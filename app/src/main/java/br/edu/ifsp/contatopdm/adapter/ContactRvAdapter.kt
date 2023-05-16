package br.edu.ifsp.contatopdm.adapter

import android.content.Context
import android.view.*
import android.view.View.OnCreateContextMenuListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.contatopdm.databinding.TileContactBinding
import br.edu.ifsp.contatopdm.model.Contact

class ContactRvAdapter(
    private val contactList: MutableList<Contact>, // data source
    private val onContactClickListener: OnContactClickListener // objeto que implementa as ações de clique
): RecyclerView.Adapter<ContactRvAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(tileContactBinding: TileContactBinding) :
        RecyclerView.ViewHolder(tileContactBinding.root), OnCreateContextMenuListener {
        val nameTv: TextView = tileContactBinding.nameTv
        val emailTv: TextView = tileContactBinding.emailTv
        var contactPosition = -1 // Para saber qual célula foi clicada
        init {
            tileContactBinding.root.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu?.add(Menu.NONE, 0, 0, "Editar")?.setOnMenuItemClickListener {
                if (contactPosition != -1) {
                    onContactClickListener.onEditMenuItemClick(contactPosition)
                }
                true
            }
            menu?.add(Menu.NONE, 1, 1, "Remover")?.setOnMenuItemClickListener {
                if (contactPosition != -1) {
                    onContactClickListener.onRemoveMenuItemClick(contactPosition)
                }
                true
            }
        }
    }

    // Chamada pela LayoutManager para buscar a quantidade de dados e preparar a quantidade de células.
    override fun getItemCount(): Int = contactList.size

    // Chamada pelo LayoutManager para criar uma nova célula (e consequentemente um novo ViewHolder).
    // Essa função só será chamada se a célula precisar ser criada mesmo.
    // se a célula existir (for reciclada) ela não será chamada.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        // Criar uma nova célula
        val tileContactBinding = TileContactBinding.inflate(LayoutInflater.from(parent.context))

         // Cria um ViewHolder usando a célula
        val contactViewHolder = ContactViewHolder(tileContactBinding)

        // Retorna o ViewHolder
        return contactViewHolder
    }

    // Chamada pelo LayoutManager para alterar os valores de uma célula
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        // Busca o contato pela posição no data source
        val contact = contactList[position]

        // Altera os valores da célula
        holder.nameTv.text = contact.name
        holder.emailTv.text = contact.email
        holder.contactPosition = position

        holder.itemView.setOnClickListener {
            onContactClickListener.onTileContactClick(position)
        }
    }


}