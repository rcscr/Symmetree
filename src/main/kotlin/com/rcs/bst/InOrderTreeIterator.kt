package com.rcs.bst

import org.example.com.rcs.bst.TreeNode

class InOrderTreeIterator<K: Comparable<K>, V>(
    val bst: AvlTree<K, V>
): Iterator<TreeEntry<K, V>> {

    companion object {
        fun <K: Comparable<K>, V> fromStart(bst: AvlTree<K, V>, start: TreeNode<K, V>?): InOrderTreeIterator<K, V> {
            val it = InOrderTreeIterator(bst)
            it.next = start
            return it
        }
    }

    private var next = TreeUtils.leftMost(bst.root)
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
        if (next?.right != null) {
            next = TreeUtils.leftMost(next?.right)
        } else {
            while (next?.parent != null && next == next?.parent?.right) {
                next = next?.parent
            }
            next = next?.parent
        }
    }
}