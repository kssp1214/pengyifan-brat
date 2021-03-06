package com.pengyifan.brat;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Maps;

public class BratDocument {

  private String text;
  private String id;
  private List<BratAnnotation> annotations;

  public BratDocument() {
    annotations = Lists.newLinkedList();
  }

  public BratDocument(BratDocument doc) {
    this();
    setText(doc.getText());
    setDocId(doc.getDocId());
    for (BratAnnotation ann : doc.getAnnotations()) {
      addAnnotation(ann);
    }
  }

  public void setText(String text) {
    this.text = text;
  }

  /**
   * Returns text of the original documents input
   * 
   * @return text of the original documents input
   */
  public String getText() {
    return text;
  }


  public boolean containsId(String id) {
    return get(id).isPresent();
  }

  private Optional<BratAnnotation> get(String id) {
    return annotations.stream()
        .filter(a -> a.getId().equals(id))
        .findAny();
  }

  public BratAnnotation getAnnotation(String id) {
    Optional<BratAnnotation> opt = get(id);
    checkArgument(opt.isPresent(), "Don't contain %s", id);
    return opt.get();
  }

  public BratEntity getEntity(String id) {
    BratAnnotation ann = getAnnotation(id);
    checkArgument(ann instanceof BratEntity, "%s is not BratEntity", id);
    return (BratEntity) ann;
  }

  public BratRelation getRelation(String id) {
    BratAnnotation ann = getAnnotation(id);
    checkArgument(ann instanceof BratRelation, "%s is not BratRelation", id);
    return (BratRelation) ann;
  }

  public BratEvent getEvent(String id) {
    BratAnnotation ann = getAnnotation(id);
    checkArgument(ann instanceof BratEvent, "%s is not BratEvent", id);
    return (BratEvent) ann;
  }

  public void addAnnotation(BratAnnotation ann) {
    if (!(ann instanceof BratEquivRelation)) {
      checkArgument(!containsId(ann.getId()), "already have %s", ann.getId());
    }
    annotations.add(ann);
  }

  public List<BratAnnotation> getAnnotations() {
    return annotations;
  }

  public List<BratEvent> getEvents() {
    return getAnnotations().stream().filter(ann -> ann instanceof BratEvent)
        .map(ann -> (BratEvent) ann).collect(Collectors.toList());
  }

  public List<BratEntity> getEntities() {
    return getAnnotations().stream().filter(ann -> ann instanceof BratEntity)
        .map(ann -> (BratEntity) ann).collect(Collectors.toList());
  }

  public List<BratRelation> getRelations() {
    return getAnnotations().stream().filter(ann -> ann instanceof BratRelation)
        .map(ann -> (BratRelation) ann).collect(Collectors.toList());
  }

  public List<BratAttribute> getAttributes() {
    return getAnnotations().stream()
        .filter(ann -> ann instanceof BratAttribute)
        .map(ann -> (BratAttribute) ann).collect(Collectors.toList());
  }

  public List<BratEquivRelation> getEquivRelations() {
    return getAnnotations().stream()
        .filter(ann -> ann instanceof BratEquivRelation)
        .map(ann -> (BratEquivRelation) ann).collect(Collectors.toList());
  }

  public List<BratNote> getNotes() {
    return getAnnotations().stream().filter(ann -> ann instanceof BratNote)
        .map(ann -> (BratNote) ann).collect(Collectors.toList());
  }

  /**
   * 
   * @param refId refereed id
   * @return list of BratNote
   */
  public List<BratNote> getNotes(String refId) {
    return getNotes().stream()
        .filter(n -> n.getRefId().equals(refId))
        .collect(Collectors.toList());
  }

  public List<BratAttribute> getAttributes(String refId) {
    return getAttributes().stream()
        .filter(a -> a.getRefId().equals(refId))
        .collect(Collectors.toList());
  }

  public void setDocId(String id) {
    this.id = id;
  }

  /**
   * Returns document id. Usually document name
   */
  public String getDocId() {
    return id;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("id", getDocId())
        .append("text", getText())
        .append("annotations", getAnnotations())
        .toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id,
        text,
        annotations);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof BratDocument)) {
      return false;
    }
    BratDocument rhs = (BratDocument) o;
    return Objects.equals(id, rhs.id)
        && Objects.equals(text, rhs.text)
        && Objects.equals(annotations, rhs.annotations);
  }
}
