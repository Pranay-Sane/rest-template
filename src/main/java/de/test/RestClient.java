package de.test;

/**
 * @author iternovykh
 *         Date: 22.04.2016.
 */
public interface RestClient
{
    Post getByID(int id);
    String getHealthCheck();
    String addPost();
}
