package com.kunyan.config

import java.io.File

import org.dom4j.io.SAXReader
import org.dom4j.{Document, Element}

import scala.collection.mutable
import scala.util.{Failure, Success, Try}

/**
  * Created by C.J.YOU on 2016/12/8.
  */
class Dom4jParser(xmlFilePath:String)  {

  private val parser = dom4jParser

  private val paremeters = new mutable.HashMap[String, String]

  def dom4jParser = {

    val file = new File(xmlFilePath)
    val saxReader = new SAXReader()

    val doc = Try(saxReader.read(file))

    // note : scala de try operation is pattern
    val result = doc match  {
      case Success(r) => r
      case Failure(exception) => println("getDocumentExcetion:" +exception.getMessage)
    }

    result.asInstanceOf[Document]

  }

  override def toString = this.xmlFilePath

  private  def elements(element: Element):  Unit= {

    val iterator = element.elementIterator()

    if(! iterator.hasNext)
      paremeters.+=(element.getName -> element.getTextTrim)

    while(iterator.hasNext) {

      val subElement:Element = iterator.next().asInstanceOf[Element]

      elements(subElement)

    }

  }

  def getAllParameter = {

    val root = parser.getRootElement

    if (paremeters.isEmpty)
      elements(root)

    paremeters

  }

}

object Dom4jParser {

  private val parser: Dom4jParser = null

  def apply(xmlFilePath: String): Dom4jParser = {

    if(parser == null)
      new Dom4jParser(xmlFilePath)
    else  parser

  }

  def getInstance = parser

}
