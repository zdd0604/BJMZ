/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.hjnerp.util.myscom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Provides utility methods and decorators for {@link List} instances.
 *
 * @since Commons Collections 1.0
 * @version $Revision: 646777 $ $Date: 2008-04-10 13:33:15 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Federico Barbieri
 * @author Peter Donald
 * @author Paul Jack
 * @author Stephen Colebourne
 * @author Neil O'Toole
 * @author Matthew Hawthorne
 */
public class ListUtils {

    //-----------------------------------------------------------------------
    /**
     * Returns a new list containing all elements that are contained in
     * both given lists.
     *
     * @param list1  the first list
     * @param list2  the second list
     * @return  the intersection of those two lists
     * @throws NullPointerException if either list is null
     */
    public static List<Object> intersection(final List list1, final List list2) {
        final ArrayList<Object> result = new ArrayList<Object>();
        final Iterator iterator = list2.iterator();

        while (iterator.hasNext()) {
            final Object o = iterator.next();

            if (list1.contains(o)) {
                result.add(o);
            }
        }

        return result;
    }

    /**
     * Subtracts all elements in the second list from the first list,
     * placing the results in a new list.
     * <p>
     * This differs from {@link List#removeAll(Collection)} in that
     * cardinality is respected; if <Code>list1</Code> contains two
     * occurrences of <Code>null</Code> and <Code>list2</Code> only
     * contains one occurrence, then the returned list will still contain
     * one occurrence.
     *
     * @param list1  the list to subtract from
     * @param list2  the list to subtract
     * @return  a new list containing the results
     * @throws NullPointerException if either list is null
     */
    public static List subtract(final List list1, final List<Object> list2) {
        final ArrayList result = new ArrayList(list1);
        final Iterator<Object> iterator = list2.iterator();

        while (iterator.hasNext()) {
            result.remove(iterator.next());
        }

        return result;
    }

    /**
     * Returns the sum of the given lists.  This is their intersection
     * subtracted from their union.
     *
     * @param list1  the first list 
     * @param list2  the second list
     * @return  a new list containing the sum of those lists
     * @throws NullPointerException if either list is null
     */ 
    public static List sum(final List list1, final List list2) {
        return subtract(union(list1, list2), intersection(list1, list2));
    }

    /**
     * Returns a new list containing the second list appended to the
     * first list.  The {@link List#addAll(Collection)} operation is
     * used to append the two given lists into a new list.
     *
     * @param list1  the first list 
     * @param list2  the second list
     * @return  a new list containing the union of those lists
     * @throws NullPointerException if either list is null
     */
    public static List union(final List list1, final List list2) {
        final ArrayList result = new ArrayList(list1);
        result.addAll(list2);
        return result;
    }

    /**
     * Tests two lists for value-equality as per the equality contract in
     * {@link java.util.List#equals(java.lang.Object)}.
     * <p>
     * This method is useful for implementing <code>List</code> when you cannot
     * extend AbstractList. The method takes Collection instances to enable other
     * collection types to use the List implementation algorithm.
     * <p>
     * The relevant text (slightly paraphrased as this is a static method) is:
     * <blockquote>
     * Compares the two list objects for equality.  Returns
     * <tt>true</tt> if and only if both
     * lists have the same size, and all corresponding pairs of elements in
     * the two lists are <i>equal</i>.  (Two elements <tt>e1</tt> and
     * <tt>e2</tt> are <i>equal</i> if <tt>(e1==null ? e2==null :
     * e1.equals(e2))</tt>.)  In other words, two lists are defined to be
     * equal if they contain the same elements in the same order.  This
     * definition ensures that the equals method works properly across
     * different implementations of the <tt>List</tt> interface.
     * </blockquote>
     *
     * <b>Note:</b> The behaviour of this method is undefined if the lists are
     * modified during the equals comparison.
     * 
     * @see java.util.List
     * @param list1  the first list, may be null
     * @param list2  the second list, may be null
     * @return whether the lists are equal by value comparison
     */
    public static boolean isEqualList(final Collection list1, final Collection list2) {
        if (list1 == list2) {
            return true;
        }
        if (list1 == null || list2 == null || list1.size() != list2.size()) {
            return false;
        }

        Iterator it1 = list1.iterator();
        Iterator it2 = list2.iterator();
        Object obj1 = null;
        Object obj2 = null;

        while (it1.hasNext() && it2.hasNext()) {
            obj1 = it1.next();
            obj2 = it2.next();

            if (!(obj1 == null ? obj2 == null : obj1.equals(obj2))) {
                return false;
            }
        }

        return !(it1.hasNext() || it2.hasNext());
    }
    
    /**
     * Generates a hash code using the algorithm specified in 
     * {@link java.util.List#hashCode()}.
     * <p>
     * This method is useful for implementing <code>List</code> when you cannot
     * extend AbstractList. The method takes Collection instances to enable other
     * collection types to use the List implementation algorithm.
     * 
     * @see java.util.List#hashCode()
     * @param list  the list to generate the hashCode for, may be null
     * @return the hash code
     */
    public static int hashCodeForList(final Collection list) {
        if (list == null) {
            return 0;
        }
        int hashCode = 1;
        Iterator it = list.iterator();
        Object obj = null;
        
        while (it.hasNext()) {
            obj = it.next();
            hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode());
        }
        return hashCode;
    }   

    //-----------------------------------------------------------------------
    /**
     * Returns a List containing all the elements in <code>collection</code>
     * that are also in <code>retain</code>. The cardinality of an element <code>e</code>
     * in the returned list is the same as the cardinality of <code>e</code>
     * in <code>collection</code> unless <code>retain</code> does not contain <code>e</code>, in which
     * case the cardinality is zero. This method is useful if you do not wish to modify
     * the collection <code>c</code> and thus cannot call <code>collection.retainAll(retain);</code>.
     * 
     * @param collection  the collection whose contents are the target of the #retailAll operation
     * @param retain  the collection containing the elements to be retained in the returned collection
     * @return a <code>List</code> containing all the elements of <code>c</code>
     * that occur at least once in <code>retain</code>.
     * @throws NullPointerException if either parameter is null
     * @since Commons Collections 3.2
     */
    public static List<Object> retainAll(Collection collection, Collection retain) {
        List<Object> list = new ArrayList<Object>(Math.min(collection.size(), retain.size()));

        for (Iterator iter = collection.iterator(); iter.hasNext();) {
            Object obj = iter.next();
            if (retain.contains(obj)) {
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * Removes the elements in <code>remove</code> from <code>collection</code>. That is, this
     * method returns a list containing all the elements in <code>c</code>
     * that are not in <code>remove</code>. The cardinality of an element <code>e</code>
     * in the returned collection is the same as the cardinality of <code>e</code>
     * in <code>collection</code> unless <code>remove</code> contains <code>e</code>, in which
     * case the cardinality is zero. This method is useful if you do not wish to modify
     * <code>collection</code> and thus cannot call <code>collection.removeAll(remove);</code>.
     * 
     * @param collection  the collection from which items are removed (in the returned collection)
     * @param remove  the items to be removed from the returned <code>collection</code>
     * @return a <code>List</code> containing all the elements of <code>c</code> except
     * any elements that also occur in <code>remove</code>.
     * @throws NullPointerException if either parameter is null
     * @since Commons Collections 3.2
     */
    public static List<Object> removeAll(Collection collection, Collection remove) {
        List<Object> list = new ArrayList<Object>();
        for (Iterator iter = collection.iterator(); iter.hasNext();) {
            Object obj = iter.next();
            if (remove.contains(obj) == false) {
                list.add(obj);
            }
        }
        return list;
    }

}
