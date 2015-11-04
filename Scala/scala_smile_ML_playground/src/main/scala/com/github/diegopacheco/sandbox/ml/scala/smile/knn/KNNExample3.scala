package com.github.diegopacheco.sandbox.ml.scala.smile.knn

import java.io.File
import smile.classification.KNN
import smile.plot._
import smile.validation._

object KNNExample3 { 
  
  def main(args: Array[String]): Unit = {
    
      val basePath = "/src/main/resources/KNN_Example_1.csv"
      val testData = getDataFromCSV( new File(new File(".").getCanonicalPath  + basePath))
  
      //Define the amount of rounds, in our case 2 and 
      //initialise the cross validation
      
      val validationRounds = 2
      
      val cv = new CrossValidation(testData._2.length, validationRounds)
  
      val testDataWithIndices = (testData
                                  ._1
                                  .zipWithIndex, 
                                  testData
                                  ._2
                                  .zipWithIndex)
  
      val trainingDPSets = cv.train
        .map(indexList => indexList
        .map(index => testDataWithIndices
        ._1.collectFirst { case (dp, `index`) => dp}.get))
  
      val trainingClassifierSets = cv.train
        .map(indexList => indexList
        .map(index => testDataWithIndices
        ._2.collectFirst { case (dp, `index`) => dp}.get))
  
      val testingDPSets = cv.test
        .map(indexList => indexList
        .map(index => testDataWithIndices
        ._1.collectFirst { case (dp, `index`) => dp}.get))
  
      val testingClassifierSets = cv.test
        .map(indexList => indexList
        .map(index => testDataWithIndices
        ._2.collectFirst { case (dp, `index`) => dp}.get))
  
  
      val validationRoundRecords = trainingDPSets
        .zipWithIndex.map(x => (  x._1,           
                                  trainingClassifierSets(x._2),
                                  testingDPSets(x._2),
                                  testingClassifierSets(x._2)
                                 )
                          )
  
      validationRoundRecords
          .foreach { record =>
  
        val knn = KNN.learn(record._1, record._2, 3)
  
        //And for each test data point make a prediction with the model
        val predictions = record
          ._3
          .map(x => knn.predict(x))
          .zipWithIndex
  
        //Finally evaluate the predictions as correct or incorrect
        //and count the amount of wrongly classified data points.
  
        val error = predictions
          .map(x => if (x._1 != record._4(x._2)) 1 else 0)
          .sum
  
        println("False prediction rate: " + error / predictions.length * 100 + "%")
        
        //
        // part 2 
        //
        println("part 2")
        
        val knn2 = KNN.learn(record._1, record._2, 3)
        val unknownDataPoint = Array(5.3, 4.3)
        val result = knn.predict(unknownDataPoint)
        
        if (result == 0){
            println("Internet Service Provider Alpha")
        }else if (result == 1){
            println("Internet Service Provider Beta")
        } else {
            println("Unexpected prediction")
        }
        
      }
     
  }
  
  def getDataFromCSV(file: File): (Array[Array[Double]], Array[Int]) = {
      val source = scala.io.Source.fromFile(file)
      val data = source
          .getLines()
          .drop(1)
          .map(x => getDataFromString(x))
          .toArray
  
      source.close()
      val dataPoints = data.map(x => x._1)
      val classifierArray = data.map(x => x._2)
      return (dataPoints, classifierArray)        
    }
  
    def getDataFromString(dataString: String): (Array[Double], Int) = {
  
      //Split the comma separated value string into an array of strings
      val dataArray: Array[String] = dataString.split(',')
  
      //Extract the values from the strings
      val xCoordinate: Double = dataArray(0).toDouble
      val yCoordinate: Double = dataArray(1).toDouble
      val classifier: Int = dataArray(2).toInt
  
      //And return the result in a format that can later 
      //easily be used to feed to Smile
      return (Array(xCoordinate, yCoordinate), classifier)
    }
  
}