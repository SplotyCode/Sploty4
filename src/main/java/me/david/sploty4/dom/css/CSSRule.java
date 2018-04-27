package me.david.sploty4.dom.css;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CSSRule {

    @Setter @Getter private List<CSSSelector> selectors;
    @Setter @Getter private List<CSSDeclaration> declarations;

    public CSSRule() {
        selectors = new ArrayList<>();
        declarations = new ArrayList<>();
    }

}
