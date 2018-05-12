package me.david.sploty4.debugger;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import me.david.sploty4.Sploty;
import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.dom.html.Node;
import me.david.sploty4.dom.html.attriute.Attribute;
import me.david.sploty4.gui.Window;
import me.david.sploty4.gui.tab.BrowserTab;
import me.david.sploty4.objects.NicePrint;

import java.lang.reflect.Field;
import java.util.*;

public class DomExplorer extends DebugTab {

    private TreeView<String> treeView;
    private BorderPane main = new BorderPane();

    public DomExplorer(final Window window, final BrowserTab tab) {
        super("DomExplorer", window, tab);
        //if (tab.getDocument() instanceof HtmlDocument) {
        //    treeView = new TreeView<>(buildTree(((HtmlDocument) tab.getDocument()).getHtml()));
        //} else treeView = new TreeView<>(new TreeItem<>("Only HtmlDocuments have a Dom Tree!"));
        //main.setCenter(new TreeView<>());
        setContent(main);
    }

    /*private TreeItem<String> buildTree(final Node node) {
        TreeItem<String> item = new TreeItem<>();
        item.setExpanded(true);

        TreeItem<String> attributes = new TreeItem<>("Attributes - " + node.getAttributes().size());
        attributes.setExpanded(node.getAttributes().size() != 0);
        for (Attribute attribute : node.getAttributes()) {
            TreeItem<String> attibute = new TreeItem<>(attribute.getName());
            for (Field field : attribute.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (!field.getName().equals("name")){
                    attibute.setExpanded(true);
                    try {
                        attibute.getChildren().add(new TreeItem<>(field.getName() + " -> " + field.get(attribute)));
                    } catch (IllegalAccessException ex) {
                        Sploty.getLogger().exception(ex, "Failed building Attributes for Dom Debugger!");
                    }
                }
            }
            attributes.getChildren().add(attibute);
        }

        TreeItem<String> childs = new TreeItem<>("Dom Child's - " + node.getChilds().size());
        childs.setExpanded(node.getChilds().size() != 0);
        for (Node child : node.getChilds())
            childs.getChildren().add(buildTree(child));

        for (Field field : node.getClass().getFields()) {
            field.setAccessible(true);
            if (!field.getName().equals("name") && !field.getName().equals("id") && !field.getName().equals("childs") && !field.getName().equals("parent") && !field.getName().equals("attributes")) {
                TreeItem<String> fItem = new TreeItem<>();
                Object obj;
                try {
                    obj = field.get(node);
                } catch (IllegalAccessException ex) {
                    Sploty.getLogger().exception(ex, "Failed building Attributes for Dom Debugger!");
                    return item;
                }

                Class<?> arraytype = obj.getClass().getComponentType();
                if (obj instanceof NicePrint) {
                    fItem.setValue(((NicePrint) obj).nicePrint());
                } else if (arraytype != null) {
                    if (!arraytype.isPrimitive()) {
                        fItem.setValue(field.getName() + " | " + Arrays.deepToString((Object[]) obj));
                    } else if (arraytype.equals(Integer.TYPE)) {
                        fItem.setValue(field.getName() + " | " + Arrays.toString((int[]) obj));
                    } else if (arraytype.equals(Double.TYPE)) {
                        fItem.setValue(field.getName() + " | " + Arrays.toString((double[]) obj));
                    } else if (arraytype.equals(Boolean.TYPE)) {
                        fItem.setValue(field.getName() + " | " + Arrays.toString((boolean[]) obj));
                    } else if (arraytype.equals(Short.TYPE)) {
                        fItem.setValue(field.getName() + " | " + Arrays.toString((short[]) obj));
                    } else if (arraytype.equals(Long.TYPE)) {
                        fItem.setValue(field.getName() + " | " + Arrays.toString((long[]) obj));
                    } else if (arraytype.equals(Float.TYPE)) {
                        fItem.setValue(field.getName() + " | " + Arrays.toString((float[]) obj));
                    } else if (arraytype.equals(Character.TYPE)) {
                        fItem.setValue(field.getName() + " | " + Arrays.toString((char[]) obj));
                    } else if (arraytype.equals(Byte.TYPE)) {
                        fItem.setValue(field.getName() + " | " + Arrays.toString((byte[]) obj));
                    } else {
                        fItem.setValue(field.getName() + " | " + "[Impossible State]");
                    }
                } else if(field.getType().isEnum()){
                    fItem.setValue(field.getName() + " | " + field.getClass().getName() + "." + ((Enum) obj).name().toUpperCase() + " (enum)");
                }else if (obj instanceof Set) {
                    Set<Object> set = (Set<Object>) obj;
                    ArrayList<String> list = new ArrayList<>(set.size());
                    for(Object sObj : set) {
                        list.add(sObj instanceof NicePrint ? ((NicePrint) sObj).nicePrint() : sObj.toString());
                    }
                    Collections.sort(list);
                    fItem.setValue(field.getName());
                    fItem.setExpanded(true);
                    for (String value : list)
                        fItem.getChildren().add(new TreeItem<>(value));
                } else if (obj instanceof Map) {
                    Map<Object,Object> map = (Map<Object, Object>) obj;
                    ArrayList<String> list = new ArrayList<>();
                    for(Object key : map.keySet()) {
                        Object value = map.get(key);
                        list.add(key instanceof NicePrint ? ((NicePrint) key).nicePrint() : key.toString() + " = " + (value instanceof NicePrint ? ((NicePrint) value).nicePrint() : value.toString()));
                    }
                    Collections.sort(list);
                    fItem.setValue(field.getName());
                    fItem.setExpanded(true);
                    for (String value : list)
                        fItem.getChildren().add(new TreeItem<>(value));
                } else if (obj instanceof List) {
                    ArrayList<String> list = new ArrayList<>();
                    for (Object i : (List) obj)
                        list.add(i instanceof NicePrint ? ((NicePrint) i).nicePrint() : i.toString());
                    Collections.sort(list);
                    fItem.setValue(field.getName());
                    fItem.setExpanded(true);
                    for (String value : list)
                        fItem.getChildren().add(new TreeItem<>(value));
                } else if (obj instanceof Collection) {
                    fItem.setValue(field.getName());
                    fItem.setExpanded(true);
                    for (Object value : (Collection) obj)
                        fItem.getChildren().add(new TreeItem<>(value instanceof NicePrint ? ((NicePrint) value).nicePrint() : value.toString()));
                } else {
                    fItem.setValue(field.getName() + " | " + obj.toString());
                }
                item.getChildren().add(fItem);
            }
        }

        item.getChildren().addAll(attributes, childs);
        return item;
    }*/
}
