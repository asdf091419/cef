// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package tests.detailed.handler;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandlerAdapter;
import tests.detailed.MainFrame;
import tests.detailed.ModelFrame;
import tests.detailed.util.RobotUtil;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.IOException;

public class MessageRouterHandler extends CefMessageRouterHandlerAdapter {
    @Override
    public boolean onQuery(CefBrowser browser, CefFrame frame, long query_id, String request,
            boolean persistent, CefQueryCallback callback) {

        int screenWidth=((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
        int screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
        System.out.println("request:"+request);
        if (request.indexOf("BindingTest:") == 0) {
            // Reverse the message and return it to the JavaScript caller.
            String msg = request.substring(12);
            callback.success(new StringBuilder(msg).reverse().toString());
            return true;
        }

        if (request.indexOf("Event:MouseDown") == 0) {
            System.out.println("MouseDown:");
            // Reverse the message and return it to the JavaScript caller.
            String msg = request.substring(16);
           // try {
                //Robot robot = RobotUtil.GetInstance();
                // 执行完一个事件后再执行下一个
                //robot.setAutoWaitForIdle(true);
                // 移动鼠标到指定屏幕坐标

                String[] tem=msg.split(",");
                // 按下鼠标左键
                Integer X=Integer.parseInt(tem[0].replace("px",""));
                Integer Y=Integer.parseInt(tem[1].replace("px",""));
                Integer Width=Integer.parseInt(tem[2].replace("px",""));
                Integer Height=Integer.parseInt(tem[3].replace("px",""));

                ModelFrame.getInstance().StartdrawLine(X,Y);

                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Double w=screenSize.getWidth();
                Double h=screenSize.getHeight();

                ModelFrame.getInstance().setRateX(w/Width);
                ModelFrame.getInstance().setRateY(h/Height);


            callback.success(new StringBuilder(msg).reverse().toString());
            return true;
        }
        if (request.indexOf("Event:MouseMove") == 0) {
            System.out.println("MouseMove:");
            // Reverse the message and return it to the JavaScript caller.
            String msg = request.substring(16);
            try {
                //Robot robot = RobotUtil.GetInstance();
                // 执行完一个事件后再执行下一个
              //  robot.setAutoWaitForIdle(true);
                // 移动鼠标到指定屏幕坐标

                String[] tem=msg.split(",");
                // 按下鼠标左键
                Integer X=Integer.parseInt(tem[0].replace("px",""));
                Integer Y=Integer.parseInt(tem[1].replace("px",""));
                ModelFrame.getInstance().drawLine(X,Y);
//                robot.mouseMove(X,Y);


              //  robot.mouseMove(300, 400);
                // 延时100毫秒
                //robot.delay(100);
                // 释放鼠标左键（按下后必须要释放, 一次点击操作包含了按下和释放）
                //robot.mouseRelease(InputEvent.BUTTON1_MASK);
            } catch (Exception e) {
                e.printStackTrace();
            }

            callback.success(new StringBuilder(msg).reverse().toString());
            return true;
        }
        if (request.indexOf("Event:MouseUp") == 0) {
            System.out.println("MouseUp:");
            // Reverse the message and return it to the JavaScript caller.
            String msg = request.substring(14);
            String[] tem=msg.split(",");
            Integer X=Integer.parseInt(tem[0].replace("px",""));
            Integer Y=Integer.parseInt(tem[1].replace("px",""));
            ModelFrame.getInstance().EnddrawLine(X,Y);
            ModelFrame modelFrame=ModelFrame.getInstance();

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            modelFrame.setSize((int)screenSize.getWidth(), (int)screenSize.getHeight());
            modelFrame.setVisible(true);
            modelFrame.setAlwaysOnTop(true);
            modelFrame.addMouseMotionListener(modelFrame);
            modelFrame.addMouseListener(modelFrame);

            callback.success(new StringBuilder(msg).reverse().toString());
            return true;
        }
        if (request.indexOf("Event:Exit") == 0) {
            MainFrame.getInstance().dispose();
            System.exit(0);
        }
        // Not handled.
        return false;
    }
}
