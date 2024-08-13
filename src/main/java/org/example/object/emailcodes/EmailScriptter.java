package org.example.object.emailcodes;


import lombok.Getter;
import lombok.Setter;
import org.example.service.internal.Workspace;
import org.example.utils.SendMails;

@Setter
@Getter
public abstract class EmailScriptter extends Workspace {

    public EmailScriptter(String to, String title){
        this.to = to;
        this.title = title;
    }

    protected String to;
    protected String title;

    interface EmailHtml{
        String writeHtml(String str);
    }

    protected void sender(String emailHtml){
        SendMails.getInstance()
                .setEMAIL(to)
                .setCONTENT((s) -> emailHtml)
                .setTITLE(title).send();
    }

}
