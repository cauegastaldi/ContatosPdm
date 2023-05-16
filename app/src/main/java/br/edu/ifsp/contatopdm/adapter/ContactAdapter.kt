package br.edu.ifsp.contatopdm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.ifsp.contatopdm.R
import br.edu.ifsp.contatopdm.databinding.TileContactBinding
import br.edu.ifsp.contatopdm.model.Contact

class ContactAdapter(
    context: Context,
    private val contactList: MutableList<Contact>
): ArrayAdapter<Contact>(context, R.layout.tile_contact, contactList) {
    private lateinit var tcb: TileContactBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val contact: Contact = contactList[position]

        var tileContactView = convertView
        if(tileContactView == null) {
            // infla uma nova célula
            tcb = TileContactBinding.inflate(
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            )
            tileContactView = tcb.root

            // criando um holder e guardando referencia para os TextView
            val tcvh = TileContactViewHolder(tcb.nameTv, tcb.emailTv)

            // armazenando ViewHolder na célula
            tileContactView.tag = tcvh
        }

        // substituir os valores
        with (tileContactView.tag as TileContactViewHolder) {
            nomeTv.text = contact.name
            emailTv.text = contact.email
        }

        return tileContactView
    }

    private data class TileContactViewHolder (
         val nomeTv: TextView,
         val emailTv: TextView
    )
}