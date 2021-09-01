package de.test;

import org.apache.commons.lang3.StringUtils;

/**
 * DTO to be fetched from remote REST resource
 *
 * @author iternovykh
 *         Date: 22.04.2016.
 */
public class Post
{
    private int userId;
    private int id;
    private String title;
    private String body;

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    @Override
    public String toString()
    {
        return String.format("id=%s, userID=%s, title=%s, body=%s", id, userId, StringUtils.abbreviate(title, 20),
                StringUtils.abbreviate(body, 20));
    }
}
