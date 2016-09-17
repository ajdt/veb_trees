/******************************************************
 *Program:
 *      VEBKey.java
 *Purpose:
 *      an interface that must be used to work with VEBGen
 *      objects. The only method required produces a unique key
 *      given a generic type as a parameter.
 *Programmer:
 *       Armando Diaz T.
 *Last Modified:
 *          March 7, 2010
 ******************************************************/
package genericveb;

public interface VEBKey {

    /*getKey()
     *  must be able to produce a unique key for
     *  given object. Key used to store item in
     *  VEBgen tree
     */
    public int getKey();
    /*toString()
     *  Object  type to String must be overridden
     *  this is used to print result of max/min &
     *  other operations
     */
    public String toString();

}
