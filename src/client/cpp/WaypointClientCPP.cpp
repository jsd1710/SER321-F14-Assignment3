#include "WaypointGUI.cpp"

#include <FL/Fl.H>
#include <FL/Fl_Window.H>
#include <FL/Fl_Button.H>
#include <FL/Fl_Output.H>
#include <FL/Fl_Text_Display.H>
#include <FL/Fl_Text_Buffer.H>
#include <FL/Fl_Input_Choice.H>
#include <FL/Fl_Multiline_Input.H>
#include <fstream>
#include <string>
#include <stdlib.h>
#include <json/json.h>


using namespace std;

/**
 * Purpose: C++ FLTK client UI for Waypoint management.
 * This class extends the Gui component class WaypointGUI and demonstrates
 * sample control functions that respond to button clicks drop-down selects.
 * This software is meant to run on OSX, and Windows Cygwin using g++.
 **/
class SampleStudentClient: public WaypointGUI
{
	/** ClickedX is one of the callbacks for GUI controls.
	 * Callbacks need to be static functions. But, static functions
	 * cannot directly access instance data. This program uses "userdata"
	 * to get around that by passing the instance to the callback
	 * function. The callback then accesses whatever GUI control object
	 * that it needs for implementing its functionality.
	 */
	static void ClickedX(Fl_Widget * w, void * userdata)
	{
		cout << "You clicked Exit" << endl;
		exit(1);
	}

	static void ClickedImportJSON(Fl_Widget * w, void * userdata)
	{
		SampleStudentClient* anInstance = (SampleStudentClient*) userdata;
		Fl_Input_Choice * fromWPChoice = anInstance->frWps;
		Fl_Input_Choice * toWPChoice = anInstance->toWps;

		Json::Value root;   // will contains the root value after parsing.
		Json::Reader reader;

		ifstream waypointsFile ("waypoints.json", ifstream::binary); //Reads waypoints.json file.

		if (waypointsFile.is_open())
		  {
		    bool parsingSuccessful = reader.parse(waypointsFile, root, false);
			if (parsingSuccessful)
			{
				for (Json::ValueIterator itr = root.begin(); itr != root.end(); itr++)
				{
					string name(itr.key().asString());
					fromWPChoice->add(name.c_str());
					toWPChoice->add(name.c_str());
					cout << "Added: " << itr.key().asString() << endl;
				}

			}
		    waypointsFile.close();
		  }



	}

	static void ClickedRemoveWP(Fl_Widget * w, void * userdata)
	{
		SampleStudentClient* anInstance = (SampleStudentClient*) userdata;
		Fl_Input_Choice * theWPChoice = anInstance->frWps;
		string selected(theWPChoice->value());
		cout << "You clicked the remove waypoint button with " << selected
				<< std::endl;
		for (int i = 0; i < theWPChoice->menubutton()->size(); i++)
		{
			const Fl_Menu_Item &item = theWPChoice->menubutton()->menu()[i];
			if (!selected.compare(item.label()))
			{  // if they are equal
				theWPChoice->menubutton()->remove(i);
				cout << "removed " << selected << endl;
				break;
			}
		}
		if (theWPChoice->menubutton()->size() > 0)
		{
			theWPChoice->value(theWPChoice->menubutton()->menu()[0].label());
		}
		else
		{
			theWPChoice->value("");
		}
	}

	static void ClickedAddWP(Fl_Widget * w, void * userdata) {
		SampleStudentClient* anInstance = (SampleStudentClient*) userdata;
		Fl_Input_Choice * fromWPChoice = anInstance->frWps;
		Fl_Input_Choice * toWPChoice = anInstance->toWps;
		Fl_Input * theLat = anInstance->latIn;
		Fl_Input * theLon = anInstance->lonIn;
		Fl_Input * theEle = anInstance->eleIn;
		Fl_Input * theName = anInstance->nameIn;
		string lat(theLat->value());
		// what follows is not expedient, but shows how to convert to/from
		// double and formatted C and C++ strings.
		double latNum = atof(lat.c_str());  //convert from string to double
		char latFormat[10];
		sprintf(latFormat, "%4.4f", latNum); //format the double into a C string
		string latCppStr(latFormat);   //convert formatted C str to C++ str

		string lon(theLon->value());
		string ele(theEle->value());
		string name(theName->value());
		cout << "You clicked the add waypoint button lat: " << latCppStr
				<< " lon: " << lon << " ele: " << ele << " name: " << name
				<< endl;
		fromWPChoice->add(name.c_str());
		toWPChoice->add(name.c_str());
		fromWPChoice->value(name.c_str());
	}

	static void SelectedFromWP(Fl_Widget * w, void * userdata) {
		SampleStudentClient* anInstance = (SampleStudentClient*) userdata;
		Fl_Input_Choice * frWps = anInstance->frWps;
		string selected(frWps->value());
		cout << "You selected from waypoint " << selected << endl;
		anInstance->latIn->value("stuff");
	}

public:
	SampleStudentClient(const char * name = 0) :
			WaypointGUI(name)
	{
		removeWPButt->callback(ClickedRemoveWP, (void*) this);
		addWPButt->callback(ClickedAddWP, (void*) this);
		frWps->callback(SelectedFromWP, (void*) this);
		importJSONButt->callback(ClickedImportJSON, (void*) this);
		callback(ClickedX);
	}
};

int main()
{
	//Waypoint wp;
	SampleStudentClient cm("Jacob Dobkins' Waypoint Browser");

	return (Fl::run());
}
