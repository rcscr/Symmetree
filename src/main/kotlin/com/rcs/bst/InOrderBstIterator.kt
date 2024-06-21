package com.rcs.bst

import org.example.com.rcs.bst.BstNode

class InOrderBstIterator<K, V>(root: BstNode<K, V>?): Iterator<BstEntry<K, V>> {

    private val visited = mutableSetOf<K>()
    private var next = leftMost(root)

    override fun hasNext(): Boolean {
        return next != null
    }

    override fun next(): BstEntry<K, V> {
        val toReturn = next!!
        visited.add(toReturn.key)
        setNext()
        return BstEntry(toReturn.key, toReturn.value)
    }

    private fun setNext() {
        if (next!!.left != null && !visited.contains(next!!.left!!.key)) {
            next = leftMost(next)
        } else if (next?.right != null) {
            next = leftMost(next!!.right)
        } else if (next!!.parent != null) {
            do {
                val previousNext = next
                next = next!!.parent
            } while (next != null && next!!.right == previousNext)
        }
    }

    private fun leftMost(root: BstNode<K, V>?): BstNode<K, V>? {
        var leftMost = root
        while (leftMost?.left != null) {
            leftMost = leftMost.left!!
        }
        return leftMost
    }
}