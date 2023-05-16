package br.edu.ifsp.contatopdm.adapter

// Interface que será implementada pela MainActivity para tratar
// eventos de clique nas células do RecyclerView e que será usada
// pelo ContactRvAdapter para tratar os eventos de clique individualmente
interface OnContactClickListener {
    fun onTileContactClick(position: Int)

    fun onEditMenuItemClick(position: Int)

    fun onRemoveMenuItemClick(position: Int)
}