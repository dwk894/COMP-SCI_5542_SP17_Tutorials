import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by dwk89 on 02/01/2017.
  */
object GPA_Calculator {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "C:\\winutils")
    val sparkConf = new SparkConf().setAppName("GPA_Calculator").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    //Count each grade from the transcript (input).
    val textFile = sc.textFile("Input\\Transcript.txt")
    val counts = textFile.flatMap(line => line.split(" "))
      .map(word => (word, 1))
      .reduceByKey(_ + _)

    //Filter the data with grades: A, B, C, D, and F.
    val A = counts.collect().filter(f => f._1 == "A")
    val B = counts.collect().filter(f => f._1 == "B")
    val C = counts.collect().filter(f => f._1 == "C")
    val D = counts.collect().filter(f => f._1 == "D")
    val F = counts.collect().filter(f => f._1 == "F")

    //Calculate grade points and credit hours.
    var points = 0.0
    var units = 0.0

    A.foreach(f => points += 4.0 * 3 * f._2)
    B.foreach(f => points += 3.0 * 3 * f._2)
    C.foreach(f => points += 2.0 * 3 * f._2)
    D.foreach(f => points += 1.0 * 3 * f._2)
    F.foreach(f => points += 0.0 * 3 * f._2)
    A.foreach(f => units += 3 * f._2)
    B.foreach(f => units += 3 * f._2)
    C.foreach(f => units += 3 * f._2)
    D.foreach(f => units += 3 * f._2)
    F.foreach(f => units += 3 * f._2)

    println("GPA = " + points/units)
  }
}
