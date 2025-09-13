import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class NewsAggregator extends JFrame {
    private JTextArea feedArea;
    private JTextField urlField;
    private JButton loadBtn;
    private DefaultListModel<String> feedListModel;
    private JList<String> feedList;

    public NewsAggregator() {
        setTitle("News Aggregator");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        feedArea = new JTextArea();
        feedArea.setEditable(false);
        feedListModel = new DefaultListModel<>();
        feedList = new JList<>(feedListModel);
        JScrollPane feedPane = new JScrollPane(feedArea);
        JScrollPane listPane = new JScrollPane(feedList);
        urlField = new JTextField("https://rss.nytimes.com/services/xml/rss/nyt/World.xml", 30);
        loadBtn = new JButton("Load Feed");

        JPanel topPanel = new JPanel();
        topPanel.add(urlField);
        topPanel.add(loadBtn);

        add(topPanel, BorderLayout.NORTH);
        add(listPane, BorderLayout.WEST);
        add(feedPane, BorderLayout.CENTER);

        loadBtn.addActionListener(e -> loadFeed());
        feedList.addListSelectionListener(e -> showArticle(feedList.getSelectedIndex()));

        setVisible(true);
    }

    private void loadFeed() {
        try {
            String url = urlField.getText();
            URL feedUrl = new URL(url);
            InputStream is = feedUrl.openStream();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("item");
            feedListModel.clear();
            for (int i = 0; i < nList.getLength(); i++) {
                Element el = (Element) nList.item(i);
                String title = el.getElementsByTagName("title").item(0).getTextContent();
                feedListModel.addElement(title);
            }
            feedArea.setText("");
        } catch (Exception ex) {
            feedArea.setText("Error loading feed: " + ex.getMessage());
        }
    }

    private void showArticle(int idx) {
        try {
            String url = urlField.getText();
            URL feedUrl = new URL(url);
            InputStream is = feedUrl.openStream();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("item");
            if (idx < 0 || idx >= nList.getLength()) return;
            Element el = (Element) nList.item(idx);
            String desc = el.getElementsByTagName("description").item(0).getTextContent();
            feedArea.setText(desc);
        } catch (Exception ex) {
            feedArea.setText("Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new NewsAggregator();
    }
}