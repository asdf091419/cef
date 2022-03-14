// Copyright (c) 2013 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package tests.detailed;


import org.cef.CefApp;
import org.cef.CefApp.CefVersion;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.CefSettings.ColorType;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.browser.CefMessageRouter;
import org.cef.handler.*;
import org.cef.network.CefCookieManager;
import org.cef.network.CefRequest;
import tests.detailed.dialog.DevToolsDialog;
import tests.detailed.dialog.DownloadDialog;
import tests.detailed.handler.*;
import tests.detailed.ui.ControlPanel;
import tests.detailed.ui.MenuBar;
import tests.detailed.ui.StatusPanel;
import tests.detailed.util.DataUri;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainFrame extends BrowserFrame {
    private static final long serialVersionUID = -2295538706810864538L;
    private static  String user=null;
    private static  String pass=null;
    private static  String roomName=null;
    private static  String serverip=null;
    private static  String port=null;
    private static  int screen=1;
    public static void main(String[] args) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println("++++++++++++++"+df.format(new Date()));// new Date()为获取当前系统时间
        // Perform startup initialization on platforms that require it.
        if (!CefApp.startup(args)) {
            System.out.println("Startup initialization failed!");
            return;
        }
        String p1=null,p2=null;
        // OSR mode is enabled by default on Linux.
        // and disabled by default on Windows and Mac OS X.
        boolean osrEnabledArg = false;
        boolean transparentPaintingEnabledArg = false;
        boolean createImmediately = false;
        for (String arg : args) {
            arg = arg.toLowerCase();
            if (arg.equals("--off-screen-rendering-enabled")) {
                osrEnabledArg = true;
            } else if (arg.equals("--transparent-painting-enabled")) {
                transparentPaintingEnabledArg = true;
            } else if (arg.equals("--create-immediately")) {
                createImmediately = true;
            }
            else if(arg.contains("--user")){
                user = arg.split("=")[1];
            }
            else if(arg.contains("--pwd")){
                pass = arg.split("=")[1];
            }
            else if(arg.contains("--roomname")){
                roomName = arg.split("=")[1];
            }else if(arg.contains("--videoscreen")){
                screen = Integer.parseInt(arg.split("=")[1]);
            }
        }

        System.out.println("Offscreen rendering " + (osrEnabledArg ? "enabled" : "disabled"));

        // MainFrame keeps all the knowledge to display the embedded browser
        // frame.
        final MainFrame frame = new MainFrame(
                osrEnabledArg, transparentPaintingEnabledArg, createImmediately, args);

        frame.setUndecorated(true);

        //showOnScreen2(screen,frame);
        showOnScreen2_Test(screen,frame);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);

        System.out.println("++++++++++++++"+df.format(new Date()));// new Date()为获取当前系统时间


        final String name = ManagementFactory.getRuntimeMXBean().getName();

        final String pid = name.split("@")[0];
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    frame.write("pid.txt",pid);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        System.out.println("++++++++++++++"+df.format(new Date()));// new Date()为获取当前系统时间
    }

    public static void showOnScreen2_Test(int screen, JFrame frame)
    {
        frame.setLocation(screen*800, 0);
        frame.setSize(800,600);
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private final CefClient client_;
    private String errorMsg_ = "";
    private ControlPanel control_pane_;
    private StatusPanel status_panel_;
    private boolean browserFocus_ = true;

    public MainFrame(boolean osrEnabled, boolean transparentPaintingEnabled,
                     boolean createImmediately, String[] args) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println("1"+df.format(new Date()));// new Date()为获取当前系统时间
        Instance = this;
        CefApp myApp;
        if (CefApp.getState() != CefApp.CefAppState.INITIALIZED) {
            // 1) CefApp is the entry point for JCEF. You can pass
            //    application arguments to it, if you want to handle any
            //    chromium or CEF related switches/attributes in
            //    the native world.
            CefSettings settings = new CefSettings();
            settings.windowless_rendering_enabled = osrEnabled;
            String v1=getLibpath();
            settings.browser_subprocess_path=v1+"\\jcef_helper.exe";
            // try to load URL "about:blank" to see the background color
          //  settings.background_color = settings.new ColorType(100, 255, 242, 211);

            myApp = CefApp.getInstance(args, settings);

            CefVersion version = myApp.getVersion();
            System.out.println("Using:\n" + version);

            //    We're registering our own AppHandler because we want to
            //    add an own schemes (search:// and client://) and its corresponding
            //    protocol handlers. So if you enter "search:something on the web", your
            //    search request "something on the web" is forwarded to www.google.com
            CefApp.addAppHandler(new AppHandler(args));
        } else {
            myApp = CefApp.getInstance();
        }
        System.out.println("1 .1 "+df.format(new Date()));// new Date()为获取当前系统时间
        //    By calling the method createClient() the native part
        //    of JCEF/CEF will be initialized and an  instance of
        //    CefClient will be created. You can create one to many
        //    instances of CefClient.
        client_ = myApp.createClient();
        System.out.println("1 .2 "+df.format(new Date()));// new Date()为获取当前系统时间
        // 2) You have the ability to pass different handlers to your
        //    instance of CefClient. Each handler is responsible to
        //    deal with different informations (e.g. keyboard input).
        //
        //    For each handler (with more than one method) adapter
        //    classes exists. So you don't need to override methods
        //    you're not interested in.
//        DownloadDialog downloadDialog = new DownloadDialog(this);
        System.out.println("1 .3 "+df.format(new Date()));// new Date()为获取当前系统时间
        client_.addContextMenuHandler(new ContextMenuHandler(this));
        System.out.println("1 .4 "+df.format(new Date()));// new Date()为获取当前系统时间
//        client_.addDownloadHandler(downloadDialog);
        System.out.println("1 .5 "+df.format(new Date()));// new Date()为获取当前系统时间
//        client_.addDragHandler(new DragHandler());
        System.out.println("1 .6 "+df.format(new Date()));// new Date()为获取当前系统时间
        client_.addJSDialogHandler(new JSDialogHandler());
        System.out.println("1 .7 "+df.format(new Date()));// new Date()为获取当前系统时间
        client_.addKeyboardHandler(new KeyboardHandler(this));
        System.out.println("1 .8 "+df.format(new Date()));// new Date()为获取当前系统时间
        client_.addRequestHandler(new RequestHandler(this));
        System.out.println("1 .9 "+df.format(new Date()));// new Date()为获取当前系统时间

        //    Beside the normal handler instances, we're registering a MessageRouter
        //    as well. That gives us the opportunity to reply to JavaScript method
        //    calls (JavaScript binding). We're using the default configuration, so
        //    that the JavaScript binding methods "cefQuery" and "cefQueryCancel"
        //    are used.
        System.out.println("2 .2 ====="+df.format(new Date()));// new Date()为获取当前系统时间
        CefMessageRouter msgRouter = CefMessageRouter.create();
        msgRouter.addHandler(new MessageRouterHandler(), true);
        msgRouter.addHandler(new MessageRouterHandlerEx(client_), false);
        client_.addMessageRouter(msgRouter);
        System.out.println("2 .1 ====="+df.format(new Date()));// new Date()为获取当前系统时间

        client_.addLoadHandler(new CefLoadHandlerAdapter() {
            @Override
            public void onLoadEnd(CefBrowser cefBrowser, CefFrame cefFrame, int i) {
                super.onLoadEnd(cefBrowser, cefFrame, i);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                System.out.println("onLoadEnd++++++++++++++"+df.format(new Date()));// new Date()为获取当前系统时间
//                if(cefBrowser.getURL().contains("114.116.110.145")){
//                    cefBrowser.loadURL("https://localhost:5000");
//                }
            }

            @Override
            public void onLoadError(CefBrowser browser, CefFrame frame, ErrorCode errorCode,
                                    String errorText, String failedUrl) {
                if (errorCode != ErrorCode.ERR_NONE && errorCode != ErrorCode.ERR_ABORTED) {
                    errorMsg_ = "<html><head>";
                    errorMsg_ += "<title>Error while loading</title>";
                    errorMsg_ += "</head><body>";
                    errorMsg_ += "<h1>" + errorCode + "</h1>";
                    errorMsg_ += "<h3>Failed to load " + failedUrl + "</h3>";
                    errorMsg_ += "<p>" + (errorText == null ? "" : errorText) + "</p>";
                    errorMsg_ += "</body></html>";
                    browser.stopLoad();
                }
            }

            @Override
            public void onLoadStart(CefBrowser cefBrowser, CefFrame cefFrame, CefRequest.TransitionType transitionType) {
                super.onLoadStart(cefBrowser, cefFrame, transitionType);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                System.out.println("onLoadStart+++++++++++++"+df.format(new Date()));// new Date()为获取当前系统时间
            }
        });
        String IP= "114.115.214.159";
        //IP= "192.168.4.171";
        // Create the browser.
        System.out.println("2"+df.format(new Date()));// new Date()为获取当前系统时间
//        CefBrowser browser = client_.createBrowser(
//                "https://"+IP+":5002/login?user="+user+"&&pass="+pass+"&&roomName="+roomName, osrEnabled, transparentPaintingEnabled, null);

        CefBrowser browser = client_.createBrowser(
                "http://localhost:8080/video_tjabc/vtm_video.html", osrEnabled, transparentPaintingEnabled, null);


        setBrowser(browser);
        System.out.println("3"+df.format(new Date()));// new Date()为获取当前系统时间
//        CefBrowser browser = client_.createBrowser(
//                "https://localhost:5000/sessionVTM", osrEnabled, transparentPaintingEnabled, null);
        setBrowser(browser);

        System.out.println("4"+df.format(new Date()));// new Date()为获取当前系统时间


        JPanel contentPanel =new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel,BoxLayout.X_AXIS));
        contentPanel.add(getBrowser().getUIComponent(),BorderLayout.CENTER);
        Panel p1=new Panel(new BorderLayout());
        p1.setSize(1,1);
        contentPanel.add(p1);
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        if (createImmediately) browser.createImmediately();
        System.out.println("5"+df.format(new Date()));// new Date()为获取当前系统时间
//      MenuBar menuBar = new MenuBar(
//                this, browser, control_pane_, null, CefCookieManager.getGlobalManager());
//        JMenuBar menuBar=new JMenuBar();


      System.out.println("6"+df.format(new Date()));// new Date()为获取当前系统时间
    }

    public static void showOnScreen2(int screen, JFrame frame)
    {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();
        if (screen > -1 && screen < gd.length)
        {
            frame.setLocation(gd[screen].getDefaultConfiguration().getBounds().x, frame.getY());
            frame.setSize(gd[screen].getDisplayMode().getWidth(),gd[screen].getDisplayMode().getHeight());
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else if (gd.length > 0)
        {
            frame.setLocation(gd[0].getDefaultConfiguration().getBounds().x, frame.getY());
            frame.setSize(gd[0].getDisplayMode().getWidth(),gd[0].getDisplayMode().getHeight());
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else
        {
            throw new RuntimeException("No Screens Found");
        }
    }

    public  void write(String path,String pid)
            throws IOException {
        //将写入转化为流的形式
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        //一次写一行
        bw.write(pid);
        //关闭流
        bw.close();
    }
    private static MainFrame Instance;
    public static MainFrame getInstance(){
        return Instance;
    }


    public String getLibpath(){
        String var0 = System.getProperty("java.library.path");
        String[] var1 = var0.split(System.getProperty("path.separator"));
        String[] var2 = var1;
        int var3 = var1.length;
        for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            File var6 = new File(var5);
            String[] var7 = var6.list(new FilenameFilter() {
                public boolean accept(File var1, String var2) {
                    return var2.equalsIgnoreCase("libjcef.dylib") || var2.equalsIgnoreCase("libjcef.so") || var2.equalsIgnoreCase("jcef.dll");
                }
            });
            if (var7 != null && var7.length != 0) {
                return var5;
            }
        }
        return var0;
    }



}
