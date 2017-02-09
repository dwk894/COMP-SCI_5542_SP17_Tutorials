import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionWithSGD}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by dwk89 on 02/08/2017.
  */
object LinearRegressionModel {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir","C:\\winutils")
    val sparkConf = new SparkConf().setAppName("LinearRegressionModel").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    // Load and parse the data.
    var data = sc.textFile("data\\LinearRegressionInput.data")
    val parsedData = data.map { line =>
      val firstComma = line.indexOf(",")
      val effectiveLine = line.substring(firstComma + 1)
      val parts = effectiveLine.split(' ')
      LabeledPoint(parts(0).toDouble, Vectors.dense(parts(1).toDouble))
    }.cache()

    // Split data into training (95%) and test (5%).
    val Array(training, test) = parsedData.randomSplit(Array(0.95, 0.05))

    // Building the model.
    val numIterations = 100
    val stepSize = 0.00000001
    val model = LinearRegressionWithSGD.train(training, numIterations, stepSize)

    // Evaluate model on training examples and compute training error.
    val valuesAndPreds = training.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }
    val MSE = valuesAndPreds.map {case(v, p) => math.pow(v - p, 2)}.mean()
    scala.tools.nsc.io.File("data/LinearRegressionOutput.txt").writeAll("Training MSE = " + MSE + '\n')

    // Evaluate model on test examples and compute training error.
    val valuesAndPreds2 = test.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }
    val MSE2 = valuesAndPreds2.map {case(v, p) => math.pow(v - p, 2)}.mean()
    scala.tools.nsc.io.File("data/LinearRegressionOutput.txt").appendAll("Test MSE = " + MSE2)
  }
}