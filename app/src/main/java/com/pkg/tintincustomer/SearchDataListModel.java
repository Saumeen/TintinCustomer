package com.pkg.tintincustomer;

import com.google.firebase.firestore.DocumentReference;

public class SearchDataListModel {
private DocumentReference documentReference;
private String name;

    public SearchDataListModel(DocumentReference documentReference, String name) {
        this.documentReference = documentReference;
        this.name = name;
    }

    public DocumentReference getDocumentReference() {
        return documentReference;
    }

    public void setDocumentReference(DocumentReference documentReference) {
        this.documentReference = documentReference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

