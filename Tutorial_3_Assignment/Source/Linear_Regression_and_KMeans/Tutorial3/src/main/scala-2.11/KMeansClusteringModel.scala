import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors

/**
  * Created by dwk89 on 02/08/2017.
  */
object KMeansClusteringModel {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir","C:\\winutils")
    val sparkConf = new SparkConf().setAppName("KMeansClusteringModel").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    // Load and parse the data.
    val data = sc.textFile("data/KMeansInput.txt")
    val parsedData = data.map { line =>
      var comma = line.indexOf(",")
      var effectiveLine = line.substring(comma + 1)
      comma = effectiveLine.indexOf(",")
      effectiveLine = effectiveLine.substring(comma + 1)
      Vectors.dense(effectiveLine.split(',').map(_.toDouble))
    }.cache()

    // Cluster the data into two classes using K-Means.
    val numClusters = 3
    val numIterations = 50
    val clusters = KMeans.train(parsedData, numClusters, numIterations)

    // Evaluate clustering by computing Within Set Sum of Squared Errors.
    val WSSSE = clusters.computeCost(parsedData)

    // Look at how the clusters are in training data by making predictions.
    scala.tools.nsc.io.File("data/KMeansOutput.txt").writeAll("K-Means clustering on traning data:\n")
    clusters.predict(parsedData).zip(parsedData).foreach { f =>
      scala.tools.nsc.io.File("data/KMeansOutput.txt").appendAll('(' + f._2.toJson.substring(19, f._2.toJson.length - 1) + ", " + f._1 + ")\n")
    }
    scala.tools.nsc.io.File("data/KMeansOutput.txt").appendAll("Within Set Sum of Squared Errors = " + WSSSE)
  }
}