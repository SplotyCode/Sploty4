package me.david.sploty4.storage;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import me.david.sploty4.Sploty;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;

public enum SQLite {

    INSTANCE;

    private Connection connection;
    private File file;

    public void connect(){
        file = new File(Sploty.getDirectory(), "siteStorage.db");
        try {
            if(!file.exists()) file.createNewFile();
            connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
            String sql = "CREATE TABLE IF NOT EXISTS sites (\n"
                    + "	url varchar(511) NOT NULL,\n"
                    + " etag varchar(1023),\n"
                    + " lastmodified varchar(255),\n"
                    + "	PRIMARY KEY (url)\n"
                    + ");";
            String sql2 = "CREATE TABLE IF NOT EXISTS failed (\n"
                    + "	url varchar(511) NOT NULL,\n"
                    + " time BIGINT,\n"
                    + "	PRIMARY KEY (url)\n"
                    + ");";
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            stmt.close();
            stmt = connection.createStatement();
            stmt.execute(sql2);
            stmt.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void updateSite(URL url, String etag, String lastmodified){
        String readlUrl = url.getHost() + ":" + url.getPort() + "/" + url.getPath();
        if(etag.length() > 1023 || readlUrl.length() > 511 || lastmodified.length() > 255){
            Platform.runLater(() -> new Alert(Alert.AlertType.WARNING, "The site send strange packets...").show());
            return;
        }
        String sql = "INSERT OR IGNORE INTO sites " +
                "(url, etag, lastmodified) VALUES(?, ?, ?);" +
                "UPDATE sites SET \n" +
                "etag=?, lastmodified=?;";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, readlUrl);
            statement.setString(2, etag);
            statement.setString(3, lastmodified);
//            statement.setString(4, etag);
  //          statement.setString(5, lastmodified);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SiteData getSiteData(URL url){
        String readlUrl = url.getHost() + ":" + url.getPort() + "/" + url.getPath();
        if(readlUrl.length() > 511){
            Platform.runLater(() -> new Alert(Alert.AlertType.WARNING, "The site send strange packets...").show());
            return null;
        }
        String sql = "SELECT etag, lastmodified FROM sites WHERE url=?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, readlUrl);
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next()) return null;
            return new SiteData(resultSet.getString("etag"), resultSet.getString("lastmodified"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getFailed(String url){
        if(url.length() > 511){
            Platform.runLater(() -> new Alert(Alert.AlertType.WARNING, "The site send strange packets...").show());
            return Long.MIN_VALUE;
        }
        String sql = "SELECT time from failed WHERE url=?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, url);
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next()) return Long.MIN_VALUE;
            return resultSet.getLong("time");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Long.MIN_VALUE;
    }

    public void saveFailed(String url){
        if(url.length() > 511){
            Platform.runLater(() -> new Alert(Alert.AlertType.WARNING, "The site send strange packets...").show());
            return;
        }
        String sql = "INSERT OR IGNORE INTO failed " +
                "(url, time) VALUES(?, ?);" +
                " UPDATE sites SET \n" +
                "url=?, time=?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            long time = System.currentTimeMillis();
            stmt.setString(1, url);
            stmt.setLong(2, time);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static class SiteData {

        private String etag, lastModified;

        public SiteData(String etag, String lastModified) {
            this.etag = etag;
            this.lastModified = lastModified;
        }

        public String getEtag() {
            return etag;
        }

        public void setEtag(String etag) {
            this.etag = etag;
        }

        public String getLastModified() {
            return lastModified;
        }

        public void setLastModified(String lastModified) {
            this.lastModified = lastModified;
        }
    }

    public void disconnect(){
        if(isOpen()) try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isOpen(){
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
