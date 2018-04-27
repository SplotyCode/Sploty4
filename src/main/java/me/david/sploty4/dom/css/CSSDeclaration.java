package me.david.sploty4.dom.css;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CSSDeclaration {

    private boolean important;
    private String name;

    public CSSDeclaration(String name) {
        this.name = name;
    }
}
