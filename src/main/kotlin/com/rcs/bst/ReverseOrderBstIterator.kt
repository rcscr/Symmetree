package com.rcs.bst

class ReverseOrderBstIterator<K: Comparable<K>, V>(
    val bst: BalancedBinarySearchTree<K, V>
): Iterator<BstEntry<K, V>> {

    private var next = BstUtils.rightMost(bst.root)
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
        if (next?.left != null) {
            next = BstUtils.rightMost(next?.left)
        } else {
            while (next?.parent != null && next == next?.parent?.left) {
                next = next?.parent
            }
            next = next?.parent
        }
    }
}