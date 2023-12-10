/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author akhmim
 */
//@RequestScoped
@SessionScoped
@ManagedBean(name = "langController")

public class LangBean {

    private String lang;
    private static Map<String, Object> countries;

    static {
        countries = new LinkedHashMap<String, Object>();
        countries.put("English", Locale.ENGLISH);
        countries.put("French", Locale.FRENCH);
    }

    public LangBean() {
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Map<String, Object> getCountries() {
        return countries;
    }

    public void setCountries(Map<String, Object> countries) {
        LangBean.countries = countries;
    }

    public void change(String lgg) {
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(lgg));
    }

//    public void defineLang() {
//
//        FacesContext ctx = FacesContext.getCurrentInstance();
//        ExternalContext extCtx = ctx.getExternalContext();
//        HttpServletRequest request = (HttpServletRequest) extCtx.getRequest();
//
//        ctx.getViewRoot().setLocale(new Locale(request.getParameter("lng")));
//        return;
//    }
    public String defineLang() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extCtx = ctx.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) extCtx.getRequest();

        ctx.getViewRoot().setLocale(new Locale(request.getParameter("lng")));
        return null;
    }

    public void countryLocalChanged(ValueChangeEvent take_event) {
        String new_lang = take_event.getNewValue().toString();
        for (Map.Entry<String, Object> entry : countries.entrySet()) {
            if (entry.getValue().toString().equals(new_lang)) {
                FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale) entry.getValue());
            }

        }
    }

}
