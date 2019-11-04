package com.huaji.memorykiller;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

 
public class HistoryPath extends LitePalSupport{ 
     
    @Column(unique = true, defaultValue = "unknown") 
    private String name; 
	
	/*@Column(nullable = false)*/
	private Long id;
	
	
	
	public String getName()
	{
        return name;
    }
	
	public Long getId()
	{
		return id;
	}

    public void setName(String name)
	{
        this.name = name;
    }
	
	public void setId(long id)
	{
		this.id = id;
	}
    
    
    
    
    
}

