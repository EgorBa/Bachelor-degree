import dataReader.DataReader;
import dataReader.DataReaderList;
import dataReader.DataReaderMatrix;
import drawingApi.DrawingApi;
import drawingApi.DrawingApiAwt;
import drawingApi.DrawingApiFx;
import model.Graph;

public class Main {
    public static void main(String[] args) {
        if (args.length == 3) {
            DrawingApi drawingApi = args[0].equals("awt") ? new DrawingApiAwt() : new DrawingApiFx();
            DataReader dataReader = args[1].equals("list") ? new DataReaderList() : new DataReaderMatrix();
            Graph graph = new Graph(drawingApi, dataReader);
            graph.drawGraph(args[2]);
        } else {
            System.out.println("USEAGE : fx/awt matrix/list file.txt");
        }
    }
}
