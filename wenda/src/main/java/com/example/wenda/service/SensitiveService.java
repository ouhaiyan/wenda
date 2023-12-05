package com.example.wenda.service;

import com.alibaba.druid.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.CharUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SensitiveService implements InitializingBean {
    //让程序在初始化的时候就先把敏感词加载进来
    @Override
    public void afterPropertiesSet() throws Exception {
        rootNode = new TrieNode();
        //读取文件
        try {
            InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                addWord(lineTxt.trim());
            }
            bufferedReader.close();
        } catch (Exception e) {
            log.info("读取敏感词文件失败" + e.getMessage());
        }
    }

    //吧读取到的词汇添加进去，构建敏感词算法树
    private void addWord(String lineText) {
        TrieNode tempNode = rootNode;//根节点
        //遍历读取到的字符
        for (int i = 0; i < lineText.length(); i++) {
            //当前遍历到的字符
            Character c = lineText.charAt(i);
            // 过滤空格
            if (isSymbol(c)) {
                continue;
            }

            //返回当前c字符的节点
            TrieNode subNodes = tempNode.getSubNodes(c);
            //如果还没有节点，就新建节点
            if (subNodes == null) {
                subNodes = new TrieNode();
                //把节点和字符添加到当前节点
                tempNode.addSubNodes(c, subNodes);
            }

            //当前节点指向下一个节点
            tempNode = subNodes;
            //判断当前遍历的是否是最后一个字符
            if (i == lineText.length() - 1) {
                //是最后一个字符，就给节点设置成最后一个节点
                tempNode.setKeyEnd(true);
            }
        }

    }

    //把对输入的字符中识别出来的敏感词汇打马赛克
    public String filter(String text) {
        if (StringUtils.isEmpty(text)) {
            return text;
        }

        StringBuffer stringBuffer = new StringBuffer();


        String replacement = "***";
        //三个节点
        TrieNode tempNode = rootNode;//根节点
        int begin = 0;//第一个节点的指针
        int position = 0;//移动的指针
        //如果不是最后一个节点的指针
        while (position < text.length()) {
            //获取当前读取到的字符
            char c = text.charAt(position);

            //非法字符处理
            if (isSymbol(c)) {
                //如果是根节点，非法字符不需要处理
                if (tempNode == rootNode) {
                    stringBuffer.append(c);
                    ++begin;
                }
                //如果是在敏感词中间，无视干扰，继续看下一个字符
                ++position;
                continue;
            }

            //当前字符在敏感词算法树中是否找到
            tempNode = tempNode.getSubNodes(c);
            //如果没有，说明这个字符不是敏感词，不用替换敏感词
            if (tempNode == null) {
                stringBuffer.append(text.charAt(begin));
                position = begin + 1;
                begin = position;
                tempNode = rootNode;//指针回到根节点
            } else if (tempNode.isKeyEnd()) {//节点能走到末尾，说明是敏感词
                //替换，
                stringBuffer.append(replacement);
                //指针复位到根节点，继续从敏感词的后面，下一个字符节点开始找
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            } else {//能找到，又没走到最后一个节点，继续对比下一个节点和下一个字符
                position++;
            }

        }
        return stringBuffer.append(text.substring(begin)).toString();

    }

    //过滤一些非法字符
    private boolean isSymbol(char c) {
        int ic = c;
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    private class TrieNode {
        //是否是最后一个节点
        private boolean end = false;
        //用来构造节点树
        private Map<Character, TrieNode> subNodes = new HashMap<Character, TrieNode>();

        //向指定位置添加节点树
        public void addSubNodes(Character key, TrieNode node) {
            subNodes.put(key, node);
        }

        //获取下一个节点
        TrieNode getSubNodes(Character key) {
            return subNodes.get(key);
        }

        //是否是最后一个节点
        boolean isKeyEnd() {
            return end;
        }

        void setKeyEnd(boolean end) {
            this.end = end;
        }
    }

    //根节点，定位
    private TrieNode rootNode = new TrieNode();
}
