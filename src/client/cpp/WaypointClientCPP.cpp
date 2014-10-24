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
#include <jsonrpc/json/json.h>


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
		map < string, Waypoint > *waypoints = &anInstance->waypoints;

		Json::Value root;   // will contains the root value after parsing.
		Json::Reader reader;

		ifstream waypointsFile("waypoints.json", ifstream::binary); //Reads waypoints.json file.

		if (waypointsFile.is_open())
		{
			bool parsingSuccessful = reader.parse(waypointsFile, root, false);
			if (parsingSuccessful)
			{
				for (Json::ValueIterator itr = root.begin(); itr != root.end(); itr++)
				{
					string name(itr.key().asString());
					double lat(
							root.get(name, "ERROR").get("lat", 0).asDouble());
					double lon(
							root.get(name, "ERROR").get("lon", 0).asDouble());
					double ele(
							root.get(name, "ERROR").get("ele", 0).asDouble());

					fromWPChoice->add(name.c_str());
					toWPChoice->add(name.c_str());

					Waypoint wp(lat, lon, ele, name.c_str());
					waypoints->insert(pair<string, Waypoint>(name.c_str(), wp));

					cout << "Added:		" << name << "(" << lat << ", " << lon << ", " << ele << ", " << name << ");" << endl;

					/*
					 cout << waypoints->at(name.c_str()).lat<< endl;
					 cout << waypoints->at(name.c_str()).lon<< endl;
					 cout << waypoints->at(name.c_str()).ele<< endl;
					 cout << waypoints->at(name.c_str()).name<< endl;
					 */

				}

			}
			waypointsFile.close();
		}
		else
		{
			cout << "Error:		No 'waypoints.json' file found." << endl;
		}



	}

	static void ClickedRemoveWP(Fl_Widget * w, void * userdata)
	{
		SampleStudentClient* anInstance = (SampleStudentClient*) userdata;
		Fl_Input_Choice * fromWPChoice = anInstance->frWps;
		Fl_Input_Choice * toWPChoice = anInstance->toWps;
		map<string, Waypoint>* waypoints = &anInstance->waypoints;

		string selected(fromWPChoice->value());

		cout << "Removed:	" << selected << ";" << endl;


		for (int i = 0; i < fromWPChoice->menubutton()->size(); i++)
		{
			bool doBreak = false;

			const Fl_Menu_Item &item = fromWPChoice->menubutton()->menu()[i];
			if (!selected.compare(item.label()))
			{  // if they are equal
				fromWPChoice->menubutton()->remove(i);
				waypoints->erase(selected);

				//cout << waypoints->at(selected).name << endl; //Should break code because it's removed.
				doBreak = true;
			}

			const Fl_Menu_Item &item2 = toWPChoice->menubutton()->menu()[i];
			if (!selected.compare(item2.label()))
			{  // if they are equal
				toWPChoice->menubutton()->remove(i);
				doBreak = true;
			}

			if (doBreak == true)
			{
				break;
			}
		}

		if (fromWPChoice->menubutton()->size() > 1 && toWPChoice->menubutton()->size() > 0)
		{
			fromWPChoice->value(fromWPChoice->menubutton()->menu()[0].label());
			toWPChoice->value(toWPChoice->menubutton()->menu()[0].label());
		}
		else
		{
			fromWPChoice->add("EMPTY");
			toWPChoice->add("EMPTY");
			fromWPChoice->value(0);
			toWPChoice->value(0);
		}
	}

	static void ClickedAddWP(Fl_Widget * w, void * userdata)
	{
		SampleStudentClient* anInstance = (SampleStudentClient*) userdata;
		Fl_Input_Choice * fromWPChoice = anInstance->frWps;
		Fl_Input_Choice * toWPChoice = anInstance->toWps;
		map<string, Waypoint>* waypoints = &anInstance->waypoints;

		Fl_Input * theLat = anInstance->latIn;
		Fl_Input * theLon = anInstance->lonIn;
		Fl_Input * theEle = anInstance->eleIn;
		Fl_Input * theName = anInstance->nameIn;

		string lat(theLat->value());
		string lon(theLon->value());
		string ele(theEle->value());
		string name(theName->value());

		Waypoint wp(atof(lat.c_str()),atof(lon.c_str()),atof(ele.c_str()),name.c_str());
		waypoints->insert(pair<string, Waypoint>(name, wp));

		cout << "Added:		" << name << "(" << lat << ", " << lon << ", " << ele << ", " << name << ");" << endl;

		fromWPChoice->add(name.c_str());
		toWPChoice->add(name.c_str());
		fromWPChoice->value(name.c_str());
	}
	static void ClickedModifyWP(Fl_Widget * w, void * userdata)
	{
		SampleStudentClient* anInstance = (SampleStudentClient*) userdata;
		map < string, Waypoint > *waypoints = &anInstance->waypoints;

		Fl_Input * theLat = anInstance->latIn;
		Fl_Input * theLon = anInstance->lonIn;
		Fl_Input * theEle = anInstance->eleIn;
		Fl_Input * theName = anInstance->nameIn;

		string lat(theLat->value());
		string lon(theLon->value());
		string ele(theEle->value());
		string name(theName->value());

		Waypoint wp(atof(lat.c_str()), atof(lon.c_str()), atof(ele.c_str()),
				name.c_str());

		waypoints->erase(name);

		waypoints->insert(pair<string, Waypoint>(name, wp));

		cout << "Modified:	" << name << "(" << lat << ", " << lon << ", " << ele << ", " << name << ");" << endl;
	}

	static void SelectedFromWP(Fl_Widget * w, void * userdata)
	{
		SampleStudentClient* anInstance = (SampleStudentClient*) userdata;
		map<string, Waypoint>* waypoints = &anInstance->waypoints;
		Fl_Input_Choice * frWps = anInstance->frWps;

		string selected(frWps->value());
		cout << "Selected:	" << selected << ";" << endl;

		anInstance->nameIn->value(waypoints->at(selected).name.c_str());
		anInstance->latIn->value(to_string(waypoints->at(selected).lat).c_str());
		anInstance->lonIn->value(to_string(waypoints->at(selected).lon).c_str());
		anInstance->eleIn->value(to_string(waypoints->at(selected).ele).c_str());
	}

public:
	SampleStudentClient(const char * name = 0) :
			WaypointGUI(name)
	{
		removeWPButt->callback(ClickedRemoveWP, (void*) this);
		addWPButt->callback(ClickedAddWP, (void*) this);
		frWps->callback(SelectedFromWP, (void*) this);
		modWPButt->callback(ClickedModifyWP, (void*) this);
		importJSONButt->callback(ClickedImportJSON, (void*) this);
		callback(ClickedX);
	}
};

int main()
{
	SampleStudentClient cm("Jacob Dobkins' Waypoint Browser");

	return (Fl::run());
}
