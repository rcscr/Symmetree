package com.rcs.bst

class PostOrderBstIterator<K: Comparable<K>, V>(
    val bst: BalancedBinarySearchTree<K, V>
): Iterator<BstEntry<K, V>> {

    private var next = BstUtils.leftMost(bst.root)
    private val modCount = bst.modCount

    override fun hasNext(): Boolean {
        return next != null
    }

    override fun next(): BstEntry<K, V> {
        if (modCount != bst.modCount) {
            throw ConcurrentModificationException()
        }

        val toReturn = next!!
        setNext()
        return BstEntry(toReturn.key, toReturn.value)
    }

    private fun setNext() {
        next = when {
            next!!.isRoot() -> null
            else -> {
                val isLeftSubtree = next!!.parent!!.left == next
                var parentsRight = next!!.parent!!.right
                if (isLeftSubtree && parentsRight != null) {
                    var newNext = BstUtils.leftMost(parentsRight)
                    while (newNext == parentsRight && parentsRight!!.right != null) {
                        parentsRight = parentsRight.right
                        newNext = BstUtils.leftMost(parentsRight)
                    }
                    newNext
                } else {
                    next!!.parent
                }
            }
        }
    }
}