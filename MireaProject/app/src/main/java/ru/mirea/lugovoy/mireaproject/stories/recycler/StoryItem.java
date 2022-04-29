package ru.mirea.lugovoy.mireaproject.stories.recycler;

public class StoryItem
{
    private String subject;
    private String text;

    public StoryItem(String name, String capital)
    {

        this.subject = name;
        this.text = capital;
    }

    public String getSubject()
    {
        return this.subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getText()
    {
        return this.text;
    }

    public void setText(String text)
    {
        this.text = text;
    }
}
