package com.rcs.bst

class ReverseOrderTreeIterator<K: Comparable<K>, V>(
    val bst: AvlTree<K, V>
): Iterator<TreeEntry<K, V>> {

    private var next = TreeUtils.rightMost(bst.root)
    private val modCount = bst.modCount

    override fun hasNext(): Boolean {
        return next != null
    }

    override fun next(): TreeEntry<K, V> {
        if (modCount != bst.modCount) {
            throw ConcurrentModificationException()
        }

        val toReturn = next!!
        setNext()
        return TreeEntry(toReturn.key, toReturn.value)
    }

    private fun setNext() {
        if (next?.left != null) {
            next = TreeUtils.rightMost(next?.left)
        } else {
            while (next?.parent != null && next == next?.parent?.left) {
                next = next?.parent
            }
            next = next?.parent
        }
    }
}