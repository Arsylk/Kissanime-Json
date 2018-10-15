package com.arsylk.kissanime;

import android.util.Log;
import com.eclipsesource.v8.V8;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Cloudflare {
    //default parameters
    private String agent = "Mozilla/5.0 (Linux; Android 7.0; Moto G (5) Build/NPPS25.137-93-8) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.137 Mobile Safari/537.36";
    private int delay = 4000;
    private Map<String, String> headers = new HashMap<>();
    private String url = null;
    private URL URL = null;

    //constructor & setters
    public Cloudflare() {

    }

    public Cloudflare setUserAgent(String agent) {
        this.agent = agent;
        return this;
    }

    public Cloudflare setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public Cloudflare setDelay(int delay) {
        this.delay = delay;
        return this;
    }

    //utils & helpers
    private void makeHeaders(String url) throws Exception {
        this.url = url;
        this.URL = new URL(url);
        this.headers.put("Accept", "*/*");
        this.headers.put("Accept-Encoding", "gzip, deflate");
        this.headers.put("Content-Type", "*/*");
        this.headers.put("Host", URL.getHost());
        this.headers.put("Origin", String.format("%s://%s/", URL.getProtocol(), URL.getHost()));
    }

    private List<String> regex(String text, String pattern) {
        try {
            Pattern pt = Pattern.compile(pattern);
            Matcher mt = pt.matcher(text);
            List<String> group = new ArrayList<>();

            while (mt.find()) {
                if (mt.groupCount() >= 1) {
                    if (mt.groupCount()>1){
                        group.add(mt.group(1));
                        group.add(mt.group(2));
                    }else group.add(mt.group(1));


                }
            }
            return group;
        }catch (NullPointerException e){
            Log.i("MATCH","null");
        }
        return null;
    }

    private double execJs(String text) {
        try{
            String js = regex(text, "setTimeout\\(function\\(\\)\\{\\s+(var s,t,o,p,b,r,e,a,k,i,n,g,f.+?\\r?\\n[\\s\\S]+?a\\.value =.+?)\\r?\\n").get(0);
            js = Pattern.compile("a\\.value = (.+ \\+ t\\.length).+").matcher(js).replaceFirst("$1");
            js = js.replaceAll("\\s{3,}[a-z](?: = |\\.).+", "").replace("t.length", String.valueOf(new URL(url).getHost().length()));
            js = js.replaceAll("[\\n\']", "");

            return V8.createV8Runtime().executeDoubleScript(js);
        }catch(Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    //algorithms & methods
    public Document bypass(String url, Connection.Method method){
        try {
            makeHeaders(url);
            Connection.Response resp = Jsoup.connect(url)
                    .ignoreHttpErrors(true)
                    .headers(headers)
                    .method(method)
                    .userAgent(agent).execute();
            System.out.println("H1: "+resp.headers());
            if(resp.statusCode() == 503) {
                return solve(resp).parse();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Connection.Response solve(Connection.Response resp) throws Exception {
        System.out.println("---------start solve:");
        Thread.sleep(delay);

        String submit_url = String.format("%s://%s/cdn-cgi/l/chk_jschl", URL.getProtocol(), URL.getHost());
        System.out.println("Submit Url: "+submit_url);

        Map<String, String> jschl = new HashMap<>();
        jschl.put("jschl_vc", regex(resp.body(),"name=\"jschl_vc\" value=\"(\\w+)\"").get(0));
        jschl.put("jschl_answer", String.valueOf(execJs(resp.body())));
        jschl.put("pass", regex(resp.body(),"name=\"pass\" value=\"(.+?)\"").get(0));

        System.out.println("---------end solve:");

        System.out.println("---------start redirect:");
        Connection.Response redirect = Jsoup.connect(submit_url)
                .ignoreHttpErrors(true)
                .followRedirects(false)
                .data(jschl)
                .referrer(url)
                .headers(headers)
                .method(Connection.Method.GET)
                .userAgent(agent).execute();
        redirect = Jsoup.connect(url)
                .ignoreHttpErrors(true)
                .followRedirects(true)
                .cookies(redirect.cookies())
                .data(jschl)
                .referrer(url)
                .headers(headers)
                .method(resp.method())
                .userAgent(agent).execute();
        System.out.println("---------end redirect:");

        if(redirect.statusCode() == 200)
            return redirect;
        else if(redirect.statusCode() == 503)
            return solve(redirect);
        return null;
    }
}
