package jabsolute;

import javax.sound.midi.*;
import java.util.Vector;

public class MidiManager {

    private static Synthesizer currentSynth;
    private static Vector availableSynths = initAvailableSynths();

    private static Synthesizer getCurrentSynth() {
        if (currentSynth == null) {
            try {
                currentSynth = MidiSystem.getSynthesizer();
            } catch (MidiUnavailableException e) {
                //System.out.println ( e.getMessage());
            }
        }
        return currentSynth;
    }

    public static Vector getAvailableSynths() {
        return availableSynths;
    }

    public static void openCurrentSynth() {
        try {
            if (!(currentSynth.isOpen())) {
                currentSynth.open();
            }
        } catch (MidiUnavailableException e) {
            //System.out.println ("Midi device unavailable");
        }
    }

    public static void setCurrentSynt(Synthesizer synth) {
        currentSynth = synth;
    }

    public static void setCurrentSynth(int idx) {
        currentSynth = (Synthesizer) availableSynths.get(idx);
    }

    private static Vector initAvailableSynths() {
        MidiDevice device = null;
        Vector rv = new Vector();
        MidiDevice.Info[] midiDeviceInfo
                = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < midiDeviceInfo.length; i++) {
            try {
                device = MidiSystem.getMidiDevice(
                        midiDeviceInfo[i]);
            } catch (MidiUnavailableException e) {
                System.err.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
            //System.out.println ( midiDeviceInfo[i].getName() );
            if (device instanceof Synthesizer) {
                rv.add(device);
            }
        }
        return rv;
    } //end initAvailableSynths
} /// end MidiManager
