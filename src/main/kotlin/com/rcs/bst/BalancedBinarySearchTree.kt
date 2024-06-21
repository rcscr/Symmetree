package com.rcs.bst

import org.example.com.rcs.bst.BstNode

class BalancedBinarySearchTree<K, V>: Iterable<BstEntry<K, V>> where K: Comparable<K> {

    private val LESS = -1
    private val EQUAL = 0
    private val GREATER = 1

    internal var root: BstNode<K, V>? = null

    val height: Int get() = root?.height() ?: 0

    fun contains(key: K): Boolean {
        return get(key) != null
    }

    fun get(key: K): V? {
        return findRecursively(key, root)?.value
    }

    fun rangeQuery(fromInclusive: K, toExclusive: K): List<BstEntry<K, V>> {
        val start = findStart(fromInclusive, root)
        return InOrderBstIterator.fromStart(start)
            .asSequence()
            .takeWhile { it.key < toExclusive }
            .toList()
    }

    /**
     * Default iterator: InOrder
     */
    override fun iterator(): Iterator<BstEntry<K, V>> {
        return InOrderBstIterator(root)
    }

    fun preOrderIterator(): Iterator<BstEntry<K, V>> {
        return PreOrderBstIterator(root)
    }

    fun postOrderIterator(): Iterator<BstEntry<K, V>> {
        return PostOrderBstIterator(root)
    }

    /**
     * Returns the previous value, if any, associated with this key
     */
    fun add(key: K, value: V): V? {
        if (root == null) {
            root = BstNode(key, value, null, null, null)
            return null
        } else {
            val newRootAndPreviousValue = insert(key, value, root!!)
            root = newRootAndPreviousValue.root
            return newRootAndPreviousValue.previousValue
        }
    }

    /**
     * Returns the previous value, if any, associated with this key
     */
    fun remove(key: K): V? {
        return remove(key, root)?.let {
            root = it.root
            it.previousValue
        }
    }

    private fun insert(key: K, value: V, node: BstNode<K, V>): NewRootAndPreviousValue<K, V> {
        val newInsertAndPreviousValue = insertRecursively(key, value, node)
        return NewRootAndPreviousValue(
            rebalance(newInsertAndPreviousValue.inserted),
            newInsertAndPreviousValue.previousValue
        )
    }

    private fun insertRecursively(key: K, value: V, node: BstNode<K, V>): NewInsertAndPreviousValue<K, V> {
        return when (key.compareTo(node.key)) {
            LESS -> when (node.left) {
                null -> {
                    node.left = BstNode(key, value, null, null, node)
                    NewInsertAndPreviousValue(node.left!!, null)
                }
                else ->
                    insertRecursively(key, value, node.left!!)
            }
            EQUAL -> {
                val previousValue = node.value
                node.value = value
                NewInsertAndPreviousValue(node, previousValue)
            }
            GREATER -> when (node.right) {
                null -> {
                    node.right = BstNode(key, value, null, null, node)
                    NewInsertAndPreviousValue(node.right!!, null)
                }
                else ->
                    insertRecursively(key, value, node.right!!)
            }
            else -> throw AssertionError()
        }
    }

    private fun findRecursively(key: K, node: BstNode<K, V>?): BstNode<K, V>? {
        return node?.let {
            when (key.compareTo(it.key)) {
                LESS -> findRecursively(key, it.left)
                EQUAL -> it
                GREATER -> findRecursively(key, it.right)
                else -> throw AssertionError()
            }
        }
    }

    private fun remove(key: K, node: BstNode<K, V>?): NewRootAndPreviousValue<K, V>? {
        return node?.let {
            when (key.compareTo(it.key)) {
                LESS -> remove(key, it.left)
                EQUAL -> NewRootAndPreviousValue(unlink(it)?.let { rebalance(it) }, it.value)
                GREATER -> remove(key, it.right)
                else -> throw AssertionError()
            }
        }
    }

    private fun unlink(node: BstNode<K, V>): BstNode<K, V>? {
        return when {
            // Case 1: Node is root and has a left child
            node.isRoot() && node.left != null -> {
                val newRoot = findLeftSuccessor(node)
                newRoot.parent?.let { it.right?.parent = newRoot }
                newRoot.parent = null
                newRoot.left = node.left!!.left
                newRoot.right = node.right
                newRoot
            }
            // Case 2: Node is root and has a right child
            node.isRoot() && node.right != null -> {
                val newRoot = findRightSuccessor(node)
                newRoot.parent?.let { it.left?.parent = newRoot }
                newRoot.parent = null
                newRoot.right = node.right!!.right
                newRoot.left = node.left
                newRoot
            }
            // Case 3: Node is not root and has a left child
            node.left != null -> {
                node.parent!!.left = node.left
                node.right?.let { node.left!!.right = it}
                root
            }
            // Case 4: Node is not root and has a right child
            node.right != null -> {
                node.parent!!.right = node.right
                node.left?.let { node.right!!.left = it}
                root
            }
            // Case 5: Node is not root and has no children (a leaf node)
            !node.isRoot() -> {
                when (node) {
                    node.parent!!.left -> node.parent!!.left = null
                    node.parent!!.right -> node.parent!!.right = null
                }
                root
            }
            // Case 6: Node is root and has no children
            else -> null
        }
    }

    private fun findRightSuccessor(node: BstNode<K, V>): BstNode<K, V> {
        return node.right?.let {
            var successor = it
            while (successor.left != null) {
                successor = successor.left!!
            }
            successor
        } ?: node
    }

    private fun findLeftSuccessor(node: BstNode<K, V>): BstNode<K, V> {
        return node.left?.let {
            var successor = it
            while (successor.right != null) {
                successor = successor.right!!
            }
            successor
        } ?: node
    }

    private fun rebalance(node: BstNode<K, V>): BstNode<K, V> {
        val leftHeight = node.left?.height() ?: 0
        val rightHeight = node.right?.height() ?: 0

        val difference = leftHeight - rightHeight

        return when {
            difference < -1 -> {
                node.right?.let {
                    if ((it.left?.height() ?: 0) > (it.right?.height() ?: 0)) {
                        node.right = rotateRight(node.right!!)
                    }
                }
                rebalance(rotateLeft(node))
            }
            difference > 1 -> {
                node.left?.let {
                    if ((it.right?.height() ?: 0) > (it.left?.height() ?: 0)) {
                        node.left = rotateLeft(node.left!!)
                    }
                }
                rebalance(rotateRight(node))
            }
            node.parent != null ->
                rebalance(node.parent!!)
            else ->
                node
        }
    }

    private fun rotateRight(node: BstNode<K, V>): BstNode<K, V> {
        // new root
        val leftChild = node.left!!

        node.left = leftChild.right
        if (leftChild.right != null) {
            leftChild.right!!.parent = node
        }

        leftChild.right = node
        leftChild.parent = node.parent

        if (node.parent != null) {
            if (node.parent!!.left == node) {
                node.parent!!.left = leftChild
            } else {
                node.parent!!.right = leftChild
            }
        }

        node.parent = leftChild

        return leftChild
    }

    private fun rotateLeft(node: BstNode<K, V>): BstNode<K, V> {
        // new root
        val rightChild = node.right!!

        node.right = rightChild.left
        if (rightChild.left != null) {
            rightChild.left!!.parent = node
        }

        rightChild.left = node
        rightChild.parent = node.parent

        if (node.parent != null) {
            if (node.parent!!.left == node) {
                node.parent!!.left = rightChild
            } else {
                node.parent!!.right = rightChild
            }
        }

        node.parent = rightChild

        return rightChild
    }

    private fun findStart(startInclusive: K, node: BstNode<K, V>?): BstNode<K, V>? {
        return node?.let {
            when (startInclusive.compareTo(it.key)) {
                LESS -> {
                    if (it.left == null || it.left!!.key > startInclusive) {
                        it
                    } else {
                        findRecursively(startInclusive, it.left)
                    }
                }
                EQUAL -> it
                GREATER -> {
                    if (it.right == null || it.right!!.key > startInclusive) {
                        it
                    } else {
                        findRecursively(startInclusive, it.right)
                    }
                }
                else -> throw AssertionError()
            }
        }
    }


    private data class NewRootAndPreviousValue<K, V>(
        val root: BstNode<K, V>?,
        val previousValue: V?
    )

    private data class NewInsertAndPreviousValue<K, V>(
        val inserted: BstNode<K, V>,
        val previousValue: V?
    )
}