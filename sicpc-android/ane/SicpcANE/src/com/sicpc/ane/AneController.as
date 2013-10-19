package com.sicpc.ane
{
	import flash.events.EventDispatcher;
	import flash.events.StatusEvent;
	import flash.external.ExtensionContext;
	
	public class AneController extends EventDispatcher
	{
		protected static var _instance:AneController;
		protected static var _context:ExtensionContext = null;
		public function AneController(enforcer:SingletonEnforcer=null)
		{
			super();
			if(!_context)
			{
				_context = ExtensionContext.createExtensionContext("com.sicpc.ane","");
				if ( !_context ) {
					throw new Error( "Volume native extension is not supported on this platform." );
				}
				//_context.addEventListener(StatusEvent.STATUS, onStatus);
			}
		}
		private function init():void {
			//_context.call( "init" );
		}
		public function dispose():void { 
			_context.dispose(); 
		}
		private function onStatus( event:StatusEvent ):void {
			trace("ANE Status "+event.toString());
		}
		public static function get instance():AneController {
			if ( !_instance ) {
				_instance = new AneController( new SingletonEnforcer() );
				_instance.init();
			}
			
			return _instance;
		}
		
		public function goVideo(fileName:String):void{
			trace("Play file "+fileName);
			_context.call("playVideo", fileName);
		}
		
		public function playVideo(fileName:String):void{
			trace("Play file "+fileName);
			_context.call("playVideo", fileName);
		}
	}
}
class SingletonEnforcer {
	
}