package top.belovedyaoo.acs.service;

import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.impl.WxCpMessageServiceImpl;
import me.chanjar.weixin.cp.bean.article.NewArticle;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.bean.message.WxCpMessageSendResult;
import org.springframework.stereotype.Service;
import top.belovedyaoo.acs.entity.po.EnterpriseConfig;
import top.belovedyaoo.acs.util.ApiUtil;
import top.belovedyaoo.acs.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业微信消息接口实现类
 *
 * @author PrefersMin
 * @version 1.6
 */
@Service
@RequiredArgsConstructor
public class SendMessageService {

    /**
     * 企业微信核心服务
     */
    private final WxCoreService wxCoreService;

    /**
     * api工具类
     */
    private final ApiUtil apiUtil;

    /**
     * 用于构建并发送课程相关消息
     *
     * @author PrefersMin
     *
     * @param title 推送的标题
     * @param message 推送的消息
     * @return 返回推送结果
     */
    public WxCpMessageSendResult pushCourse(String title, String message, EnterpriseConfig enterpriseConfig) {

        try {
            // 微信消息对象
            WxCpMessageServiceImpl wxCpMessageService = new WxCpMessageServiceImpl(wxCoreService.getWxCpService(enterpriseConfig));
            WxCpMessage pushCourse = new WxCpMessage();
            pushCourse.setSafe("0");
            // 设置消息类型
            pushCourse.setMsgType("textcard");
            // 设置发送用户
            pushCourse.setToUser(apiUtil.getParticipants(enterpriseConfig));
            // 发送的标题
            pushCourse.setTitle(title);
            // 发送内容
            pushCourse.setDescription(message);
            // 设置跳转；可以自己制作一个网页
            pushCourse.setUrl(enterpriseConfig.url());
            pushCourse.setBtnTxt("PrefersMin");
            System.out.println(pushCourse);

            return wxCpMessageService.send(pushCourse);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 用于发送纯文本消息
     *
     * @author PrefersMin
     *
     * @param message 推送的消息
     * @return 返回推送结果
     */
    public WxCpMessageSendResult sendTextMsg(String message,EnterpriseConfig enterpriseConfig) {

        try {
            WxCpMessageServiceImpl wxCpMessageService = new WxCpMessageServiceImpl(wxCoreService.getWxCpService(enterpriseConfig));
            WxCpMessage textMsg = new WxCpMessage();
            textMsg.setSafe("0");
            // 设置消息类型
            textMsg.setMsgType("text");
            // 设置发送用户
            textMsg.setToUser(apiUtil.getParticipants(enterpriseConfig));
            textMsg.setContent(message);
            LogUtil.info(textMsg.toString());
            return wxCpMessageService.send(textMsg);
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 用于发送纯文本消息
     *
     * @author PrefersMin
     *
     * @param message 推送的消息
     *
     * @param sendUser 推送的用户
     */
    public void sendTextMsg(String message, String sendUser,EnterpriseConfig enterpriseConfig) {

        try {
            WxCpMessageServiceImpl wxCpMessageService = new WxCpMessageServiceImpl(wxCoreService.getWxCpService(enterpriseConfig));
            WxCpMessage textMsg = new WxCpMessage();
            textMsg.setSafe("0");
            // 设置消息类型
            textMsg.setMsgType("text");
            // 设置发送用户
            textMsg.setToUser(sendUser);
            textMsg.setContent(message);
            LogUtil.info(textMsg.toString());

            wxCpMessageService.send(textMsg);
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 用于发送图文消息
     *
     * @author PrefersMin
     *
     * @param title 推送的标题
     * @param message 推送的消息
     * @return 返回推送结果
     */
    public WxCpMessageSendResult sendNewsMsg(String title, String message,EnterpriseConfig enterpriseConfig) {

        try {
            // 微信消息对象
            WxCpMessageServiceImpl wxCpMessageService = new WxCpMessageServiceImpl(wxCoreService.getWxCpService(enterpriseConfig));
            WxCpMessage newsMsg = new WxCpMessage();
            newsMsg.setSafe("0");
            // 设置消息类型
            newsMsg.setMsgType("news");
            // 设置发送用户
            newsMsg.setToUser(apiUtil.getParticipants(enterpriseConfig));

            List<NewArticle> articlesList = new ArrayList<>();
            NewArticle newArticle = new NewArticle();
            // 发送的标题
            newArticle.setTitle(title);
            // 按钮文本
            newArticle.setBtnText("PrefersMin");
            // 发送内容
            newArticle.setDescription(message);
            // 图片地址
            newArticle.setPicUrl(enterpriseConfig.imgUrl());
            // 设置跳转；可以自己制作一个网页
            newArticle.setUrl(enterpriseConfig.imgUrl());
            // 添加到List集合
            articlesList.add(newArticle);
            newsMsg.setArticles(articlesList);
            LogUtil.info(newsMsg.toString());

            return wxCpMessageService.send(newsMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
