package application;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;



public class Main extends Application {
	Compress c;
	Decompress d;
	Button compress = new Button ("Compress");
	Button decompress = new Button ("Decompress");
	Button huff = new Button ("Huffman Code");
	Button sta = new Button ("Statistics");
	Button fileImport1 = new Button("Choose a file ");
	Label l = new Label ();
	//Button fileImport2 = new Button("Choose File to decompress");
	static File file;
	String filePath;
	private ListView table = new ListView();
	//static Node []node = new Node [256];
	//static int numOfByte=0; //Orginal File Size
	Stage stage2;
	Scene s2;
	Alert alert = new Alert(AlertType.ERROR); // error messages

	@Override
	public void start(Stage primaryStage) throws FileNotFoundException {
		
		huff.setDisable(true);
		sta.setDisable(true);
		compress.setDisable(true);
		decompress.setDisable(true);
		fileImport1.setMinSize(400, 40);
		compress.setMinSize(150,50);
		decompress.setMinSize(150,50);
		huff.setMinSize(150,50);
		sta.setMinSize(150,50);

		HBox v = new HBox();
v.getChildren().add(fileImport1);
HBox b1 = new HBox();
		b1.setSpacing(20);
		b1.getChildren().addAll(compress,decompress);
		HBox b2 = new HBox();
		b2.setSpacing(20);
		b2.getChildren().addAll(huff,sta);
		VBox v2 = new VBox();
		v2.setSpacing(20);
		v2.getChildren().addAll(v,b1,b2);

		//v.getChildren().addAll(fileImport1);
		BorderPane p = new  BorderPane ();
		//p.setTop(v);
		p.setCenter(v2);
		p.setBottom(l);
		fileImport1.setOnAction(e -> {

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Load File");

			file = fileChooser.showOpenDialog(null);
			filePath = file.getPath();
			compress.setDisable(false);
			decompress.setDisable(false);
			l.setText("");

		});
		decompress.setOnAction(e->{
			if (filePath.endsWith(".huf")) {

				d = new Decompress(filePath);
				try {
					d.createMap();
					d.readHuffFile();
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				alert.setContentText("You should select files with extention .huf , retry again!");
				alert.showAndWait();
			}
			l.setText("Decompress Done");
		});

		compress.setOnAction(e->{
			if (filePath.endsWith(".huf")) {
				alert.setContentText("You should not select files with extention .huf , retry again!");
				alert.showAndWait();
			}
			c = new Compress();
			c.setFile(filePath);
			try {
				c.readFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			c.createHeap();
			//c.map=c.createMap();
			c.setMap(c.createMap());
			try {
				c.HuffmanFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			huff.setDisable(false);
			sta.setDisable(false);
			decompress.setDisable(true);
			l.setText("Compress Done");
 
		});

		huff.setOnAction(e->{

			s2= new Scene (table,400,400);
			table.getItems().add("Byte"+"\t\t"+"char"+"\t\t"+"freq"+"\t\t"+"code");

			for (int i=0;i<c.getMap().length;i++)
				table.getItems().add(c.getMap()[i].byteCount+"\t\t"+(char)c.getMap()[i].byteCount+"\t\t"+c.getMap()[i].intCount+"\t\t"+c.getMap()[i].Huffman);

			Stage newWindow = new Stage();
			newWindow.setScene(s2);

			// Set position of second window, related to primary window.
			newWindow.setX(primaryStage.getX() + 200);
			newWindow.setY(primaryStage.getY() + 100);

			newWindow.show();

		});


		sta.setOnAction(e->{
			Header header=c.header;
double ratio= ((double)c.getCompresionByte()/header.getFileSize());
			TextArea text = new TextArea();
			text.setText("File Name :" +file.getName()+"\nOrginal File Size : "+header.getFileSize()+
					"\nCompressed File Size "+c.getCompresionByte()
					+ "\nCompression Ratio: " + ratio);



			Scene s3 = new Scene(text,300,200); 
			Stage newWindow2 = new Stage();
			newWindow2.setScene(s3);

			// Set position of second window, related to primary window.
			newWindow2.setX(primaryStage.getX() + 200);
			newWindow2.setY(primaryStage.getY() + 100);

			newWindow2.show();
		});
		Scene s = new Scene (p,400,400);
		primaryStage.setScene(s);
		primaryStage.show();
	}




	public static void main(String[] args) throws IOException {
		launch(args);


	}
}
