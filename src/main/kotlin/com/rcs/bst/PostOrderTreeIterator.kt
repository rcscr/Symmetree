package com.rcs.bst

class PostOrderTreeIterator<K: Comparable<K>, V>(
    val bst: AvlTree<K, V>
): Iterator<TreeEntry<K, V>> {

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
        next = when {
            next!!.isRoot() -> null
            else -> {
                val isLeftSubtree = next!!.parent!!.left == next
                var parentsRight = next!!.parent!!.right
                if (isLeftSubtree && parentsRight != null) {
                    var newNext = TreeUtils.leftMost(parentsRight)
                    while (newNext == parentsRight && parentsRight!!.right != null) {
                        parentsRight = parentsRight.right
                        newNext = TreeUtils.leftMost(parentsRight)
                    }
                    newNext
                } else {
                    next!!.parent
                }
            }
        }
    }
}